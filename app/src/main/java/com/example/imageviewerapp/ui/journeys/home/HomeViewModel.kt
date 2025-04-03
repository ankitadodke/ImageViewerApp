package com.example.imageviewerapp.ui.journeys.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imageviewerapp.domain.repo.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private var currentPage = 1
    private var isSearchActive = false

    init {
        loadImages()
    }

    fun loadImages() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            try {
                val result = if (isSearchActive && _searchQuery.value.isNotEmpty()) {
                    imageRepository.searchImages(_searchQuery.value, currentPage)
                } else {
                    imageRepository.getImages(currentPage)
                }

                result.fold(
                    onSuccess = { images ->
                        val currentImages = if (_uiState.value is HomeUiState.Success && currentPage > 1) {
                            (_uiState.value as HomeUiState.Success).images
                        } else {
                            emptyList()
                        }
                        _uiState.value = HomeUiState.Success(currentImages + images)
                    },
                    onFailure = { exception ->
                        _uiState.value = HomeUiState.Error(exception.message ?: "Unknown error")
                    }
                )
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadNextPage() {
        currentPage++
        loadImages()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.isEmpty() && isSearchActive) {
            isSearchActive = false
            currentPage = 1
            loadImages()
        }
    }

    fun onSearchExecute() {
        if (_searchQuery.value.isNotEmpty()) {
            isSearchActive = true
            currentPage = 1
            loadImages()
            isSearchActive = false
        }
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val images: List<Any>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}