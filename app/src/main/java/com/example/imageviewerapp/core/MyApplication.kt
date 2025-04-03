package com.example.imageviewerapp.core

import android.app.Application
import com.unsplash.pickerandroid.photopicker.UnsplashPhotoPicker
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        UnsplashPhotoPicker.init(
            application = this,
            accessKey = "_up0wwA54ag-FbTDPvwZ0LKWqtWuEnsbCTu4zd1Hb7U",
            secretKey = "nxdooiaOxJI3-ZAMHpuHecAXhIfqDOZ9duStGLrxvmM",
            pageSize = 20
        )
    }
}
