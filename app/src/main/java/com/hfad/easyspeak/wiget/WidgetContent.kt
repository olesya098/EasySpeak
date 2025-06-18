package com.hfad.easyspeak.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.hfad.easyspeak.MainActivity
import com.hfad.easyspeak.R
import androidx.glance.Image
import androidx.glance.layout.size
import androidx.glance.unit.ColorProvider
import com.hfad.easyspeak.presentation.navigation.NavigationRoutes
import com.hfad.easyspeak.ui.theme.blue

@Composable
fun WidgetContent(context: Context, totalTimeText: String) {
    // Создаем Intent для открытия MainActivity с указанием экрана
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        // Добавляем флаг для открытия MainScreen
        putExtra("destination", NavigationRoutes.MainScreenUI.route)
    }

    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(blue))
            .clickable(actionStartActivity(intent))
    ) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            Box(
                modifier = GlanceModifier
                    .background(ColorProvider(Color.White))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Vertical.CenterVertically
                ) {
                    Image(
                        provider = ImageProvider(R.drawable.clock),
                        contentDescription = "Time icon",
                        modifier = GlanceModifier.size(24.dp)
                    )
                    Text(
                        text = "Time in app: $totalTimeText",
                        style = TextStyle(
                            color = ColorProvider(Color.Black),
                            fontSize = 16.sp
                        ),
                        modifier = GlanceModifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}