package com.example.imageviewerapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.imageviewerapp.domain.model.Links
import com.example.imageviewerapp.domain.model.ProfileImage
import com.example.imageviewerapp.domain.model.Social
import com.example.imageviewerapp.domain.model.UnsplashImage
import com.example.imageviewerapp.domain.model.Urls
import com.example.imageviewerapp.domain.model.User
import com.example.imageviewerapp.domain.model.UserLinks

@Entity(tableName = "favorite_images")
data class FavoriteImage(
    @PrimaryKey
    val id: String,
    val slug: String,
    val createdAt: String?,
    val updatedAt: String?,
    val promotedAt: String?,
    val width: Int?,
    val height: Int?,
    val color: String?,
    val blurHash: String?,
    val description: String?,
    val altDescription: String?,
    val likes: Int?,
    val userId: String,
    val userName: String,
    val userUsername: String,
    val userProfileImage: String,
    val regularUrl: String,
    val fullUrl: String,
    val rawUrl: String,
    val smallUrl: String,
    val thumbUrl: String,
    val downloadUrl: String
)

// Extension function to convert from FavoriteImage to UnsplashImage
fun FavoriteImage.toUnsplashPhoto() = UnsplashImage(
    id = id,
    slug = slug,
    created_at = createdAt,
    updated_at = updatedAt,
    promoted_at = promotedAt,
    width = width,
    height = height,
    color = color,
    blur_hash = blurHash,
    description = description,
    alt_description = altDescription,
    breadcrumbs = emptyList(), // No breadcrumbs in local entity
    urls = Urls(
        raw = rawUrl,
        full = fullUrl,
        regular = regularUrl,
        small = smallUrl,
        thumb = thumbUrl,
        small_s3 = ""
    ),
    links = Links("", "", "", ""), // Placeholder links
    likes = likes,
    liked_by_user = false, // Default false
    current_user_collections = emptyList(),
    sponsorship = null,
    topic_submissions = emptyMap(),
    asset_type = null,
    user = User(
        id = userId,
        updated_at = "",
        username = userUsername,
        name = userName,
        first_name = "",
        last_name = null,
        twitter_username = null,
        portfolio_url = null,
        bio = null,
        location = null,
        links = UserLinks("", "", "", "", ""),
        profile_image = ProfileImage(
            small = "",
            medium = userProfileImage,
            large = ""
        ),
        instagram_username = null,
        total_collections = 0,
        total_likes = 0,
        total_photos = 0,
        total_promoted_photos = 0,
        total_illustrations = 0,
        total_promoted_illustrations = 0,
        accepted_tos = false,
        for_hire = false,
        social = Social(null, null, null, null)
    ),
    isFav = false
)

fun UnsplashImage.toFavoriteEntity() = FavoriteImage(
    id = id,
    slug = slug,
    createdAt = created_at,
    updatedAt = updated_at,
    promotedAt = promoted_at,
    width = width,
    height = height,
    color = color,
    blurHash = blur_hash,
    description = description,
    altDescription = alt_description,
    likes = likes,
    userId = user?.id ?: "",
    userName = user?.name ?: "",
    userUsername = user?.username ?: "",
    userProfileImage = user?.profile_image?.medium ?: "",
    regularUrl = urls.regular,
    fullUrl = urls.full,
    rawUrl = urls.raw,
    smallUrl = urls.small,
    thumbUrl = urls.thumb,
    downloadUrl = links.download
)
