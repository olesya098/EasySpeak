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
    object WordpracticeUI: NavigationRoutes("WordpracticeUI")
    object TextsUI: NavigationRoutes("TextsUI")
    object ProfilUI: NavigationRoutes("ProfilUI")
    object Exercise_Word: NavigationRoutes("Exercise_Word")
    object Exercise_ListeningUI: NavigationRoutes("Exercise_ListeningUI")
    object TextRead : NavigationRoutes("TextRead/{title}/{text}") {
        fun createRoute(title: String, text: String) = "TextRead/${encode(title)}/${encode(text)}"

        private fun encode(param: String): String {
            return java.net.URLEncoder.encode(param, "UTF-8")
        }
    }
}