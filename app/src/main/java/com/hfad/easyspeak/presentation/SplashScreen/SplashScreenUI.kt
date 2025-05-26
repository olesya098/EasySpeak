package com.hfad.easyspeak.presentation.SplashScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.F
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hfad.easyspeak.R
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
        // Видео приветствия
        WelcomeVideoPlayer(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        )

    }
}