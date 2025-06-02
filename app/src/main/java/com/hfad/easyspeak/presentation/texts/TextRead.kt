package com.hfad.easyspeak.presentation.texts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hfad.easyspeak.R
import com.hfad.easyspeak.presentation.components.CustomScaffold
import com.hfad.easyspeak.presentation.navigation.NavigationRoutes

@Composable
fun TextRead(
    navController: NavController,
    title: String?,
    text: String?
) {
    val decodedTitle = title?.let { java.net.URLDecoder.decode(it, "UTF-8") }
    val decodedText = text?.let { java.net.URLDecoder.decode(it, "UTF-8") }

    CustomScaffold(
        title = decodedTitle ?: "Текст",
        navigationIcon = {
        },
        textButton = stringResource(R.string.evertexst),
        text1 = "",
        text2 = "",
        onText2Click = {

        },
        onButtonClick = {
            navController.navigate(NavigationRoutes.TextsUI.route)
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = decodedText ?: "Текст не загружен",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSystemInDarkTheme()) Color.White else Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    )
}