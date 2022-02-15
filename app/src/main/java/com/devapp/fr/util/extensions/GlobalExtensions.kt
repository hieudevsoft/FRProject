package com.devapp.fr.util.extensions

import android.app.Activity
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

fun Fragment.convertLongToDateVn(date: Long): String {
    val timeZoneUTC = TimeZone.getDefault()
    val offsetFromUTC = timeZoneUTC.getOffset(Date().time) * -1
    val simpleFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    val date = Date(date + offsetFromUTC)
    return simpleFormat.format(date)
}

fun Activity.convertLongToDateVn(date: Long): String {
    val timeZoneUTC = TimeZone.getDefault()
    val offsetFromUTC = timeZoneUTC.getOffset(Date().time) * -1
    val simpleFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    val date = Date(date + offsetFromUTC)
    return simpleFormat.format(date)
}