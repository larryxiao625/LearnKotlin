package com.example.core

import android.app.Application
import android.content.Context
import com.example.core.http.HttpClient

class BaseApplication: Application() {

    companion object {
        lateinit var currentApplication: Context
            private set
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        currentApplication = this
    }
}