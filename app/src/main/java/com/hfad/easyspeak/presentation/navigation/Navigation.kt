package com.hfad.easyspeak.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hfad.easyspeak.presentation.exercise_word.ExerciseWord
import com.hfad.easyspeak.presentation.login.LogInUI
import com.hfad.easyspeak.presentation.mainscreen.MainScreenUI
import com.hfad.easyspeak.presentation.nointernetconnection.NoInternetConectionUI
import com.hfad.easyspeak.presentation.onboarding.VideoPlayerPreview
import com.hfad.easyspeak.presentation.pleaseregistration.PleaseRegistration
import com.hfad.easyspeak.presentation.profil.ProfilUI
import com.hfad.easyspeak.presentation.signup.SignUpUI
import com.hfad.easyspeak.presentation.signup2.SignUpUI2
import com.hfad.easyspeak.presentation.splashscreen.SplashScreenUI
import com.hfad.easyspeak.presentation.texts.TextRead
import com.hfad.easyspeak.presentation.texts.TextsUI
import com.hfad.easyspeak.presentation.wordpractice.WordpracticeUI

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
            composable(NavigationRoutes.WordpracticeUI.route) {
                WordpracticeUI(navController)
            }
            composable(NavigationRoutes.TextsUI.route) {
                TextsUI(navController)
            }
            composable(
                route = NavigationRoutes.TextRead.route,
                arguments = listOf(
                    navArgument("title") { type = NavType.StringType },
                    navArgument("text") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                TextRead(
                    navController = navController,
                    title = backStackEntry.arguments?.getString("title"),
                    text = backStackEntry.arguments?.getString("text")
                )
            }
            composable(NavigationRoutes.ProfilUI.route) {
                ProfilUI(navController)
            }
            composable(NavigationRoutes.Exercise_Word.route) {
                ExerciseWord(navController)
            }
        }
    }
}