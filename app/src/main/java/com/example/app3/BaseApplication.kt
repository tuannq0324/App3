package com.example.app3

import android.app.Application
import com.example.app3.api.RequesterImpl

class BaseApplication: Application() {

    companion object {
        lateinit var instance: BaseApplication

        val requester by lazy {
            RequesterImpl()
        }
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
    }

}