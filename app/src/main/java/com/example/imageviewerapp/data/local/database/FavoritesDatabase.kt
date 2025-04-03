package com.example.imageviewerapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.imageviewerapp.data.local.dao.FavoriteImageDao
import com.example.imageviewerapp.data.local.entity.FavoriteImage

@Database(
    entities = [FavoriteImage::class],
    version = 1,
    exportSchema = false
)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun favoriteImageDao(): FavoriteImageDao
}