package com.example.imageviewerapp.data.remote.model

import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto

data class PhotoModel(
    val id: String,
    val description: String?,
    val regularUrl: String?,
    val thumbUrl: String?,
    val user: String
) {
    companion object {
        fun fromUnsplashPhoto(photo: UnsplashPhoto): PhotoModel {
            return PhotoModel(
                id = photo.id,
                description = photo.description,
                regularUrl = photo.urls.regular,
                thumbUrl = photo.urls.thumb,
                user = photo.user.name
            )
        }
    }
}
