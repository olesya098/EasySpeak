package com.hfad.easyspeak.presentation.MainScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.hfad.easyspeak.presentation.components.CustomScaffoldMainScreen

@Composable
fun MainScreenUI(navController: NavController) {
CustomScaffoldMainScreen(
    content = { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        )
    }
)
}