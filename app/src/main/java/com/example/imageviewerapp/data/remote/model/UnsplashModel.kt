package com.example.imageviewerapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class UnsplashImageSearch(
    val id: String,
    val urls: ImageUrls,
    val user: UnsplashUser?,
    val width: Int,
    val height: Int,
    val description: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("alt_description") val altDescription: String?,
    val links: UnsplashLinks
)

data class ImageUrls(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String
)

data class UnsplashUser(
    val id: String,
    val username: String,
    val name: String,
    @SerializedName("profile_image") val profileImage: ProfileImage
)

data class ProfileImage(
    val small: String,
    val medium: String,
    val large: String
)

data class UnsplashLinks(
    val self: String,
    val html: String,
    val download: String,
    @SerializedName("download_location") val downloadLocation: String
)

data class UnsplashSearchResponse(
    val total: Int,
    @SerializedName("total_pages") val totalPages: Int,
    val results: List<UnsplashImageSearch>
)