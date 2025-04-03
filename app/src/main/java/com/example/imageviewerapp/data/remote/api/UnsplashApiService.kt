package com.example.imageviewerapp.data.remote.api

import com.example.imageviewerapp.data.remote.model.UnsplashSearchResponse
import com.example.imageviewerapp.domain.model.UnsplashImage
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApiService {
    @GET("photos")
    suspend fun getPhotos(
        @Query("client_id") id: String = "_up0wwA54ag-FbTDPvwZ0LKWqtWuEnsbCTu4zd1Hb7U",
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30
    ): List<UnsplashImage>

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("client_id") id: String = "_up0wwA54ag-FbTDPvwZ0LKWqtWuEnsbCTu4zd1Hb7U",
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30
    ): UnsplashSearchResponse

    @GET("photos/{id}")
    suspend fun getPhoto(
        @Query("id") id: String
    ): UnsplashImage
}