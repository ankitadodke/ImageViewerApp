package com.example.imageviewerapp.domain.model


data class UnsplashImage(
    val id: String,
    val slug: String,
    val created_at: String?,
    val updated_at: String?,
    val promoted_at: String?,
    val width: Int?,
    val height: Int?,
    val color: String?,
    val blur_hash: String?,
    val description: String?,
    val alt_description: String?,
    val breadcrumbs: List<String>?,
    val urls: Urls,
    val links: Links,
    val likes: Int?,
    val liked_by_user: Boolean,
    val current_user_collections: List<String>,
    val sponsorship: String?,
    val topic_submissions: Map<String, TopicSubmission>?,
    val asset_type: String?,
    val user: User?,
    val isFav : Boolean?
)

data class Urls(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String,
    val small_s3: String
)

data class Links(
    val self: String,
    val html: String,
    val download: String,
    val download_location: String
)

data class TopicSubmission(
    val status: String,
    val approved_on: String
)

data class User(
    val id: String,
    val updated_at: String,
    val username: String,
    val name: String,
    val first_name: String,
    val last_name: String?,
    val twitter_username: String?,
    val portfolio_url: String?,
    val bio: String?,
    val location: String?,
    val links: UserLinks,
    val profile_image: ProfileImage,
    val instagram_username: String?,
    val total_collections: Int,
    val total_likes: Int,
    val total_photos: Int,
    val total_promoted_photos: Int,
    val total_illustrations: Int,
    val total_promoted_illustrations: Int,
    val accepted_tos: Boolean,
    val for_hire: Boolean,
    val social: Social
)

data class UserLinks(
    val self: String,
    val html: String,
    val photos: String,
    val likes: String,
    val portfolio: String
)

data class ProfileImage(
    val small: String,
    val medium: String,
    val large: String
)
data class Social(
    val instagram_username: String?,
    val portfolio_url: String?,
    val twitter_username: String?,
    val paypal_email: String?
)
