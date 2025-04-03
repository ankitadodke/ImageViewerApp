package com.example.imageviewerapp.di.modules

import android.content.Context
import androidx.room.Room
import com.example.imageviewerapp.data.local.FavoriteDatabase
import com.example.imageviewerapp.data.local.dao.FavoriteImageDao
import com.example.imageviewerapp.data.remote.api.UnsplashApiService
import com.example.imageviewerapp.data.repo.ImageRepositoryImpl
import com.example.imageviewerapp.domain.repo.ImageRepository
import com.example.imageviewerapp.utility.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideUnsplashApi(okHttpClient: OkHttpClient): UnsplashApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UnsplashApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideFavoriteDatabase(@ApplicationContext context: Context): FavoriteDatabase {
        return Room.databaseBuilder(
            context,
            FavoriteDatabase::class.java,
            "favorite_images.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideFavoriteImageDao(database: FavoriteDatabase): FavoriteImageDao {
        return database.favoriteImageDao()
    }

    @Provides
    @Singleton
    fun provideImageRepository(
        unsplashApiService: UnsplashApiService,
        favoriteImageDao: FavoriteImageDao
    ): ImageRepository {
        return ImageRepositoryImpl(unsplashApiService, favoriteImageDao)
    }
}