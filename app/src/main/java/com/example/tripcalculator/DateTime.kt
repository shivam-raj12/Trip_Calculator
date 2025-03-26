package com.example.tripcalculator

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val DATE_FORMAT = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
private val TIME_FORMAT = SimpleDateFormat("hh:mm a", Locale.ENGLISH)

fun getSimpleFormattedDate(millisecond: Long): String {
    return SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(Date(millisecond))
}

fun getStandardFormattedDate(millisecond: Long): String {

    val currentDate = DATE_FORMAT.format(Date(System.currentTimeMillis()))
    val lastUpdateDate = DATE_FORMAT.format(Date(millisecond))

    return if (currentDate == lastUpdateDate) {
        "Today, ${TIME_FORMAT.format(Date(millisecond))}"
    } else if (
        (currentDate.substring(0..1).toInt() - lastUpdateDate.substring(0..1).toInt() == 1) &&
        (currentDate.substring(3) == lastUpdateDate.substring(3))
    ) {
        "Yesterday, ${TIME_FORMAT.format(Date(millisecond))}"
    } else {
        lastUpdateDate
    }
}