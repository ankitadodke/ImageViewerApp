package com.example.imageviewerapp.data.repo

import com.example.imageviewerapp.data.local.dao.FavoriteImageDao
import com.example.imageviewerapp.data.local.entity.FavoriteImage
import com.example.imageviewerapp.data.local.entity.toFavoriteEntity
import com.example.imageviewerapp.data.local.entity.toUnsplashPhoto
import com.example.imageviewerapp.data.remote.api.UnsplashApiService
import com.example.imageviewerapp.data.remote.model.ProfileImage
import com.example.imageviewerapp.data.remote.model.UnsplashImageSearch
import com.example.imageviewerapp.data.remote.model.UnsplashUser
import com.example.imageviewerapp.domain.model.UnsplashImage
import com.example.imageviewerapp.domain.repo.ImageRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val unsplashApiService: UnsplashApiService,
    private val favoriteImageDao: FavoriteImageDao
) : ImageRepository {

    override suspend fun getImages(page: Int): Result<List<UnsplashImage>> {
        return try {
            val remoteImages = unsplashApiService.getPhotos(page = page)
            val domainImages = remoteImages.map {
                UnsplashImage(
                    id = it.id,
                    slug = it.slug,
                    created_at = it.created_at,
                    updated_at = it.updated_at,
                    promoted_at = it.promoted_at,
                    width = it.width,
                    height = it.height,
                    color = it.color,
                    blur_hash = it.blur_hash,
                    description = it.description ?: it.alt_description
                    ?: "No description available",
                    alt_description = it.alt_description,
                    breadcrumbs = it.breadcrumbs,
                    urls = it.urls,
                    links = it.links,
                    likes = it.likes,
                    liked_by_user = it.liked_by_user,
                    current_user_collections = it.current_user_collections,
                    sponsorship = it.sponsorship,
                    topic_submissions = it.topic_submissions,
                    asset_type = it.asset_type,
                    user = it.user,
                    isFav = it.isFav
                )

            }
            Result.success(domainImages)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchImages(query: String, page: Int): Result<List<UnsplashImageSearch>> {
        return try {
            val searchResponse = unsplashApiService.searchPhotos(query = query, page = page)
            val domainImages = searchResponse.results.map {
                UnsplashImageSearch(
                    id = it.id,
                    width = it.width,
                    height = it.height,
                    description = it.description ?: "No description available",
                    urls = it.urls,
                    links = it.links,
                    user = UnsplashUser("", "", "", ProfileImage("", "", "")),
                    createdAt = "",
                    altDescription = ""
                )
            }
            Result.success(domainImages)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun favoriteImage(image: UnsplashImage) {
        favoriteImageDao.insertFavorite(image.toFavoriteEntity())
    }


    override suspend fun unfavoriteImage(imageId: String) {
        val favoriteImage = FavoriteImage(
            id = imageId,
            regularUrl = "",
            fullUrl = "",
            downloadUrl = "",
            userName = "",
            userUsername = "",
            userProfileImage = "",
            description = null,
            width = 0,
            height = 0,
            color = "",
            slug = "",
            createdAt = "",
            updatedAt = "",
            promotedAt = "",
            blurHash = "",
            altDescription = "",
            likes = 0,
            userId = "",
            rawUrl = "",
            smallUrl = "",
            thumbUrl = "",
        )
        favoriteImageDao.deleteFavorite(favoriteImage)
    }

    override fun getFavoriteImages(): Flow<List<UnsplashImage>> {
        return favoriteImageDao.getAllFavorites().map { favorites ->
            favorites.map { it.toUnsplashPhoto() }
        }
    }

    override fun isImageFavorite(imageId: String): Flow<Boolean> {
        return favoriteImageDao.isFavorite(imageId)
    }
}
