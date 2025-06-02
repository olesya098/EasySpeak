package com.hfad.easyspeak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hfad.easyspeak.presentation.nointernetconnection.MainApp
import com.hfad.easyspeak.ui.theme.EasySpeakTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EasySpeakTheme {
                MainApp()
            }
        }
    }
}

