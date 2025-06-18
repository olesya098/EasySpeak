// MainActivity.kt
package com.hfad.easyspeak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.glance.appwidget.updateAll
import androidx.navigation.compose.rememberNavController
import com.hfad.easyspeak.presentation.navigation.NavigationRoutes
import com.hfad.easyspeak.presentation.nointernetconnection.MainApp
import com.hfad.easyspeak.ui.theme.EasySpeakTheme
import com.hfad.easyspeak.widget.AppWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val destination = intent.getStringExtra("destination")

        setContent {
            EasySpeakTheme {
                if (destination != null) {
                    val navController = rememberNavController()
                    LaunchedEffect(destination) {
                        navController.navigate(destination) {
                            popUpTo(NavigationRoutes.MainScreenUI.route) {
                                inclusive = true
                            }
                        }
                    }
                }
                MainApp((application as EasySpeakApp).timeTracker)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // Сохраняем время при уходе из приложения
        (application as EasySpeakApp).timeTracker.endSession()
        // Обновляем виджет в корутине
        scope.launch {
            AppWidget().updateAll(this@MainActivity)
        }
    }

    override fun onResume() {
        super.onResume()
        // Возобновляем отсчет времени при возвращении в приложение
        (application as EasySpeakApp).timeTracker.startSession()
    }
}