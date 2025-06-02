package com.hfad.easyspeak.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    background = bigText, //задний фон
    primary = white, //title
    secondary = white.copy(alpha = 0.6f), //subtitle
    surface = blue,
    onPrimary = deepBlue,
    scrim = white,//buttom
    outline = orange,//оранжевый
    inverseOnSurface = Color(0xFF525661), //точка
    surfaceContainerLow = white.copy(alpha = 0.6f),//skip onboarding
    inversePrimary = white.copy(alpha = 0.6f), // РЯДОМ С TEXT FIELD
    surfaceContainer = smallText, //текст который ввожу
    inverseSurface = white.copy(alpha = 0.8f),//Цвет text field
    onSurfaceVariant = grayDark.copy(alpha = 0.5f),//placeHolder
    error = pink,//forgor password
    onErrorContainer = grayDark, //ты не участник?
    onTertiaryContainer = Color(0xFF000000),//mother language color text
    surfaceContainerLowest = beige,//для карточек выбора языка
    surfaceContainerHighest = Color(0xFFD9D9D9),//Круг где должен быть image user`а
    primaryContainer = lightLightGray, //top user
    onSecondaryContainer = green,//зелёный
    surfaceContainerHigh = Color(0xFFFFFFFF), //текст
    tertiary = Color(0xFF1B2336), //для фона на странице выбора фото
    tertiaryContainer = gray,//текст карточки wordpractice
    onTertiary = purple

)

//СВЕТЛАЯ
private val LightColorScheme = lightColorScheme(
    background = white,
    primary = bigText, //title
    secondary = bigText.copy(alpha = 0.6f), //subtitle
    surface = blue,
    onPrimary = deepBlue,
    scrim = white,//buttom
    outline = orange,//оранжевый
    inverseOnSurface = Color(0xFFCECFD2), //точка
    surfaceContainerLow = bigText,//skip onboarding
    inversePrimary = smallText, // РЯДОМ С TEXT FIELD
    surfaceContainer = smallText, //текст который ввожу
    inverseSurface = bigText.copy(alpha = 0.08f),//Цвет text field
    onSurfaceVariant = grayDark.copy(alpha = 0.5f),//placeHolder
    error = pink,//forgor password
    onErrorContainer = grayDark, //ты не участник?
    onTertiaryContainer = Color(0xFF000000),//mother language color text
    surfaceContainerLowest = beige,//для карточек выбора языка
    surfaceContainerHighest = Color(0xFFD9D9D9),//Круг где должен быть image user`а
    primaryContainer = lightLightGray, //top user
    onSecondaryContainer = green,//зелёный
    surfaceContainerHigh = Color(0xFF000000), //текст
    tertiary = Color(0xFF1B2336), //для фона на странице выбора фото
    tertiaryContainer = gray,//текст карточки wordpractice
    onTertiary = purple


)
val gradient = Brush.linearGradient(
    colors = listOf(
        blue.copy(alpha = 0.6f),
        blue,
        blue,
        blue,
        blue,
        blue,
        blue,
        blue,
        blue
    ),
)

@Composable
fun EasySpeakTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
    MaterialTheme(
        content = content,
        shapes = Shapes(),
        typography = Typography,
        colorScheme = colors
    )
}