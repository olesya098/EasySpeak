// AppWidget.kt
package com.hfad.easyspeak.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import com.hfad.easyspeak.model.TimeTracker

class AppWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val timeTracker = (context.applicationContext as com.hfad.easyspeak.EasySpeakApp).timeTracker
        val totalTimeToday = timeTracker.getTotalTimeTodayFormatted()

        provideContent {
            WidgetContent(context, totalTimeToday)
        }
    }
}