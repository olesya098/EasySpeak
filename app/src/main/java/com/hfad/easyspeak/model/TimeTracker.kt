package com.hfad.easyspeak.model


import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import java.util.*

class TimeTracker(private val activity: Activity) {
    private val prefs: SharedPreferences = activity.getSharedPreferences("TimeTrackerPrefs", Context.MODE_PRIVATE)
    private var sessionStartTime: Long = 0
    private var totalTimeToday: Long = 0

    fun startSession() {
        sessionStartTime = System.currentTimeMillis()

        // Проверяем, не изменился ли день
        val lastDate = prefs.getLong("last_date", 0)
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        if (lastDate != today) {
            // Новый день - сбрасываем счетчик
            prefs.edit().putLong("total_time_today", 0).apply()
            prefs.edit().putLong("last_date", today).apply()
        }

        // Загружаем сохраненное общее время за сегодня
        totalTimeToday = prefs.getLong("total_time_today", 0)
    }

    fun getCurrentSessionTime(): Long {
        return System.currentTimeMillis() - sessionStartTime
    }

    fun getCurrentSessionTimeFormatted(): String {
        val seconds = getCurrentSessionTime() / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        return when {
            hours > 0 -> "$hours ч ${minutes % 60} мин"
            minutes > 0 -> "$minutes мин"
            else -> "$seconds сек"
        }
    }

    fun getTotalTimeToday(): Long {
        return totalTimeToday + getCurrentSessionTime()
    }

    fun getTotalTimeTodayFormatted(): String {
        val seconds = getTotalTimeToday() / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        return when {
            hours > 0 -> "$hours ч ${minutes % 60} мин"
            minutes > 0 -> "$minutes мин"
            else -> "$seconds сек"
        }
    }

    fun endSession() {
        totalTimeToday += getCurrentSessionTime()
        prefs.edit().putLong("total_time_today", totalTimeToday).apply()
    }
}