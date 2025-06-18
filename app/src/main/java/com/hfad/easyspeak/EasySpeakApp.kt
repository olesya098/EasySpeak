// EasySpeakApp.kt
package com.hfad.easyspeak

import android.app.Application
import com.hfad.easyspeak.model.TimeTracker

class EasySpeakApp : Application() {
    lateinit var timeTracker: TimeTracker

    override fun onCreate() {
        super.onCreate()
        timeTracker = TimeTracker(this)
        timeTracker.startSession()
    }
}