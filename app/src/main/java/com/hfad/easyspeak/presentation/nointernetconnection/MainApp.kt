// MainApp.kt
package com.hfad.easyspeak.presentation.nointernetconnection

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.hfad.easyspeak.model.TimeTracker
import com.hfad.easyspeak.presentation.navigation.Navigation
import kotlinx.coroutines.delay

@Composable
fun MainApp(timeTracker: TimeTracker) {

    val context = LocalContext.current
    // Создаем экземпляр SharedPreferences вне remember
    val sharedPref = remember {
        context.getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE)
    }
    val networkStatus = remember { NetworkStatus(context) }
    var isOnline by remember { mutableStateOf(networkStatus.isOnline()) }
    // Состояние для хранения форматированного времени
    var totalTimeText by remember { mutableStateOf(timeTracker.getTotalTimeTodayFormatted()) }

    // Запускаем бесконечный цикл обновления времени
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000) // Обновляем каждую секунду
            // Получаем обновленное отформатированное время
            totalTimeText = timeTracker.getTotalTimeTodayFormatted()
        }
    }


    Navigation(
        isOnline = isOnline,
        totalTimeText = totalTimeText,
        onCheckConnection = {
            isOnline = networkStatus.isOnline()
        }
    )
}
