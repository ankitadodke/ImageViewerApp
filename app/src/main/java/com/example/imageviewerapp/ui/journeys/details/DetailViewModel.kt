package com.example.imageviewerapp.ui.detail

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imageviewerapp.domain.model.UnsplashImage
import com.example.imageviewerapp.domain.repo.ImageRepository
import com.example.imageviewerapp.ui.journeys.details.DetailUiState
import com.example.imageviewerapp.utility.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private var currentImage: UnsplashImage? = null

    private val imageId: String = checkNotNull(savedStateHandle["imageId"])

    init {
        loadImageDetails()
        checkFavoriteStatus()
    }

    private fun loadImageDetails() {
        viewModelScope.launch {
            try {
                val result = imageRepository.getImages(1)
                result.fold(
                    onSuccess = { images ->
                        val image = images.firstOrNull { it.id == imageId }
                        if (image != null) {
                            currentImage = image
                            _uiState.value = DetailUiState.Success(image)
                        } else {
                            _uiState.value = DetailUiState.Error("Image not found")
                        }
                    },
                    onFailure = { exception ->
                        _uiState.value = DetailUiState.Error(exception.message ?: "Unknown error")
                    }
                )
            } catch (e: Exception) {
                _uiState.value = DetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun checkFavoriteStatus() {
        viewModelScope.launch {
            imageRepository.isImageFavorite(imageId).collectLatest { isFavorite ->
                val currentState = _uiState.value
                if (currentState is DetailUiState.Success) {
                    _uiState.value =
                        DetailUiState.Success(currentState.image.copy(isFav = isFavorite))
                    currentImage = currentImage?.copy(isFav = isFavorite)
                }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            currentImage?.let { image ->
                if (image.liked_by_user) {
                    imageRepository.unfavoriteImage(image.id)
                } else {
                    imageRepository.favoriteImage(image)
                }
            }
        }
    }

    fun downloadImage(context: Context, imageUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()

                val inputStream: InputStream = connection.inputStream
                val fileName = "unsplash_${System.currentTimeMillis()}.jpg"

                // Save the image based on Android version
                val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    saveImageOnAndroid10AndAbove(inputStream, fileName)
                } else {
                    saveImageBelowAndroid10(inputStream, fileName)
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Image saved successfully", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Failed to save image: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun saveImageOnAndroid10AndAbove(inputStream: InputStream, fileName: String): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                "${Environment.DIRECTORY_PICTURES}/${Constants.IMAGE_DOWNLOAD_FOLDER}"
            )
        }

        val contentResolver = context.contentResolver
        val uri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            contentResolver.openOutputStream(it)?.use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        return uri
    }

    private fun saveImageBelowAndroid10(inputStream: InputStream, fileName: String): Uri? {
        val directory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            Constants.IMAGE_DOWNLOAD_FOLDER
        )

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, fileName)

        FileOutputStream(file).use { outputStream ->
            inputStream.copyTo(outputStream)
        }

        return Uri.fromFile(file)
    }
}