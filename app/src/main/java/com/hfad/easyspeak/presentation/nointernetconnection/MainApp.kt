package com.hfad.easyspeak.presentation.nointernetconnection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.hfad.easyspeak.presentation.navigation.Navigation
import kotlinx.coroutines.delay

@Composable
fun MainApp() {
    val context = LocalContext.current
    val networkStatus = remember { NetworkStatus(context) }
    var isOnline by remember { mutableStateOf(networkStatus.isOnline()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            isOnline = networkStatus.isOnline()
        }
    }

    Navigation(isOnline = isOnline) {
        isOnline = networkStatus.isOnline()
    }
}
