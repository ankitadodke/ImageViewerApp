package com.example.imageviewerapp.ui.journeys.Favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imageviewerapp.domain.model.UnsplashImage
import com.example.imageviewerapp.domain.repo.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = FavoritesUiState.Loading

            try {
                imageRepository.getFavoriteImages().collectLatest { favoriteImages ->
                    _uiState.value = if (favoriteImages.isEmpty()) {
                        FavoritesUiState.Empty
                    } else {
                        FavoritesUiState.Success(favoriteImages)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = FavoritesUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun removeFromFavorites(imageId: String) {
        viewModelScope.launch {
            imageRepository.unfavoriteImage(imageId)
        }
    }
}

sealed class FavoritesUiState {
    object Loading : FavoritesUiState()
    object Empty : FavoritesUiState()
    data class Success(val images: List<UnsplashImage>) : FavoritesUiState()
    data class Error(val message: String) : FavoritesUiState()
}