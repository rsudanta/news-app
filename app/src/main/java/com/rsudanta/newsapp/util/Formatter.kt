package com.rsudanta.newsapp.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun String.dateToText(): String {
    var convertTime = ""
    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val publishedTime = sdf.parse(this)
        val nowTime = Date()
        val dateDiff: Long = nowTime.time - publishedTime.time

        val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
        val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
        val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
        val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)

        if (second < 60) {
            convertTime = "$second ${if (second > 1) "seconds" else "second"} ago"
        } else if (minute < 60) {
            convertTime = "$minute ${if (minute > 1) "minutes" else "minute"} ago"
        } else if (hour < 24) {
            convertTime = "$hour ${if (hour > 1) "hours" else "hour"} ago"
        } else if (day >= 7) {
            convertTime = if (day > 360) {
                "${day / 360} ${if ((day / 360) > 1) "years" else "year"} ago"
            } else if (day > 30) {
                "${day / 30}  ${if ((day / 30) > 1) "months" else "month"} ago"
            } else {
                "${day / 7}  ${if ((day / 7) > 1) "weeks" else "week"} ago"
            }
        } else if (day < 7) {
            convertTime = "$day ${if (day > 1) "days" else "day"} ago"
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return convertTime
}
