package com.example.imageviewerapp.domain.repo

import com.example.imageviewerapp.data.remote.model.UnsplashImageSearch
import com.example.imageviewerapp.domain.model.UnsplashImage
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    suspend fun getImages(page: Int): Result<List<UnsplashImage>>
    suspend fun searchImages(query: String, page: Int): Result<List<UnsplashImageSearch>>
    suspend fun favoriteImage(image: UnsplashImage)
    suspend fun unfavoriteImage(imageId: String)
    fun getFavoriteImages(): Flow<List<UnsplashImage>>
    fun isImageFavorite(imageId: String): Flow<Boolean>
}