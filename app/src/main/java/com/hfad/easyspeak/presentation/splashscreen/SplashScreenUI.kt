package com.hfad.easyspeak.presentation.splashscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hfad.easyspeak.presentation.components.WelcomeVideoPlayer

@Composable
fun SplashScreenUI(
    navController: NavController,
    viewModel: SplashScreenViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.navigateAfterDelay(navController)
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