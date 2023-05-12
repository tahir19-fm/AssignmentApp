package com.example.assignmentapp

import android.app.Application
import android.content.Context
import android.location.LocationManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AssignmentApp:Application() {
    companion object {
        @JvmStatic
        var instance: AssignmentApp? = null
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
    }
}