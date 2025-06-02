package com.hfad.easyspeak.presentation.splashscreen

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.hfad.easyspeak.presentation.navigation.NavigationRoutes
import kotlinx.coroutines.delay

//class SplashScreenViewModel : ViewModel() {
//    companion object {
//        const val SPLASH_DELAY = 3500L
//    }
//
//    suspend fun navigateAfterDelay(navController: NavController) {
//        delay(SPLASH_DELAY)
//        navController.navigate(NavigationRoutes.VideoPlayerPreview.route) {
//            popUpTo(NavigationRoutes.SplashScreenUI.route) {
//                inclusive = true
//            }
//        }
//    }
//}

class SplashScreenViewModel : ViewModel() {
    companion object {
        const val SPLASH_DELAY = 3200L
    }

    suspend fun navigateAfterDelay(navController: NavController) {
        delay(SPLASH_DELAY)

        // Проверяем, авторизован ли пользователь
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        val destination = if (currentUser != null) {
            // Если пользователь уже вошел, переходим на главный экран
            NavigationRoutes.MainScreenUI.route
        } else {
            // Иначе на экран регистрации
            NavigationRoutes.VideoPlayerPreview.route
        }

        navController.navigate(destination) {
            popUpTo(NavigationRoutes.SplashScreenUI.route) {
                inclusive = true
            }
        }
    }
}