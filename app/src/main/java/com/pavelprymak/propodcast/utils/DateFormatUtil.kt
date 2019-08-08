package com.pavelprymak.propodcast.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateFormatUtil {
    val MS_AT_SEC = 1000
    val PUBLISH_DATE_FORMAT = SimpleDateFormat("yy.MM.dd", Locale.ENGLISH)

    fun formatTimeHHmmss(sec: Int): String {
        val hour = sec % 86400 / 3600
        val min = sec % 3600 / 60
        val second = sec % 60

        if (hour > 0) {
            return String.format("%02d:%02d:%02d", hour, min, second, Locale.ENGLISH)
        }
        return if (min > 0) {
            String.format("00:%02d:%02d", min, second, Locale.ENGLISH)
        } else String.format("00:00:%02d", second, Locale.ENGLISH)

    }
}
