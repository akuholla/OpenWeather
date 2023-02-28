package com.akshayholla.openweather.common

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    fun getTimeFromEpoch(s: String): String? {
        return try {
            val sdf = SimpleDateFormat("HH:mm")
            val netDate = Date(s.toLong() * 1000)
            sdf.format(netDate)
        } catch (e: Exception) {
            "--:--"
        }
    }
}