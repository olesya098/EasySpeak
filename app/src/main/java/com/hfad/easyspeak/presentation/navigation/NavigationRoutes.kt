package com.hfad.easyspeak.presentation.navigation

sealed class NavigationRoutes(val route: String) {
    object SplashScreenUI: NavigationRoutes("SplashScreenUI")
    object NoInternetConectionUI: NavigationRoutes("NoInternetConectionUI")
    object LogInUI: NavigationRoutes("LogInUI")
    object PleaseRegistration: NavigationRoutes("PleaseRegistration")
    object SignUpUI: NavigationRoutes("SignUpUI")
    object SignUpUI2: NavigationRoutes("SignUpUI2")
    object MainScreenUI: NavigationRoutes("MainScreenUI")
    object VideoPlayerPreview: NavigationRoutes("VideoPlayerPreview")
}