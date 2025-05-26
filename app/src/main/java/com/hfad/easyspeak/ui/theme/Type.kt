package com.hfad.easyspeak.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hfad.easyspeak.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    //title
    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.montserratalternatesregular)),
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.montserratalternatesregular)),
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.montserratalternatesbold)),
    )

//coopablacka2
    //dearlovely2
    //domcasualbt2
    //eightypercent
    //lapsusprobold
)