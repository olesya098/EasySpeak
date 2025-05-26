package com.hfad.easyspeak.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hfad.easyspeak.presentation.LogIn.LogInUI
import com.hfad.easyspeak.presentation.MainScreen.MainScreenUI
import com.hfad.easyspeak.presentation.NoInternetConection.NoInternetConectionUI
import com.hfad.easyspeak.presentation.Onboarding.VideoPlayerPreview
import com.hfad.easyspeak.presentation.PleaseRegistration.PleaseRegistration
import com.hfad.easyspeak.presentation.SignUp.SignUpUI
import com.hfad.easyspeak.presentation.SignUp2.SignUpUI2
import com.hfad.easyspeak.presentation.SplashScreen.SplashScreenUI

@Composable
fun Navigation(
    isOnline: Boolean,
    onCheckConnection: () -> Unit
) {
    val navController = rememberNavController()

    if (!isOnline) {
        NoInternetConectionUI(
            navController = navController,
            onRetry = onCheckConnection
        )
    } else {
        NavHost(
            navController = navController,
            startDestination = NavigationRoutes.SplashScreenUI.route
        ) {
            composable(NavigationRoutes.SplashScreenUI.route) {
                SplashScreenUI(navController)
            }
            composable(NavigationRoutes.NoInternetConectionUI.route) {
                NoInternetConectionUI(
                    navController = navController,
                    onRetry = onCheckConnection
                )
            }
            composable(NavigationRoutes.LogInUI.route) {
                LogInUI(navController)
            }
            composable(NavigationRoutes.PleaseRegistration.route) {
                PleaseRegistration(navController)
            }
            composable(NavigationRoutes.SignUpUI.route) {
                SignUpUI(navController)
            }
            composable(NavigationRoutes.SignUpUI2.route) {
                SignUpUI2(navController)
            }
            composable(NavigationRoutes.MainScreenUI.route) {
                MainScreenUI(navController)
            }
            composable(NavigationRoutes.VideoPlayerPreview.route) {
                VideoPlayerPreview(navController)
            }
        }
    }
}