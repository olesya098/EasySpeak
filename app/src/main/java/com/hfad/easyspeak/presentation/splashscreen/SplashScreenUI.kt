package com.hfad.easyspeak.presentation.splashscreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hfad.easyspeak.presentation.components.WelcomeVideoPlayer
import com.hfad.easyspeak.presentation.mainscreen.MainScreenViewModel
import com.hfad.easyspeak.presentation.navigation.NavigationRoutes
import kotlinx.coroutines.delay

@Composable
fun SplashScreenUI(
    navController: NavController,
    mainScreenViewModel: MainScreenViewModel,
    viewModel: SplashScreenViewModel = viewModel()
) {
    val currentUser = mainScreenViewModel.currentUser.collectAsState()
    LaunchedEffect(currentUser.value) {
        delay(3400L)
        if (currentUser.value != null){
            try {
                mainScreenViewModel.getUser(currentUser.value!!)
                navController.navigate(NavigationRoutes.MainScreenUI.route)
            }catch (e: Exception){
                Log.e("N", e.toString())
                navController.navigate(NavigationRoutes.LogInUI.route)
            }
        }else{
            navController.navigate(NavigationRoutes.VideoPlayerPreview.route)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WelcomeVideoPlayer(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        )

    }
}