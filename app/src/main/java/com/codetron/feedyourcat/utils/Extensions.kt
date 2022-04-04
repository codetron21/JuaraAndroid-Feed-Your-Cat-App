package com.codetron.feedyourcat.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.formatString(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(this)
}

fun Int.toHourMinute(): String {
    val hour = (this / 60).toString()
    val minute = (this % 60).toString()
    return "${checkDigit(hour)}:${checkDigit(minute)}"
}

private fun checkDigit(digit: String): String {
    return if (digit.length == 1) "0$digit"
    else digit
}