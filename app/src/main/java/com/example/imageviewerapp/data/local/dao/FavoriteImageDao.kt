package com.example.imageviewerapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.imageviewerapp.data.local.entity.FavoriteImage
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteImage: FavoriteImage)

    @Delete
    suspend fun deleteFavorite(favoriteImage: FavoriteImage)

    @Query("SELECT * FROM favorite_images")
    fun getAllFavorites(): Flow<List<FavoriteImage>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_images WHERE id = :imageId)")
    fun isFavorite(imageId: String): Flow<Boolean>
}