package app.botia.android.customers.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getTimeWithFormat(format: String, timestamp: Int): String {
    val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
    val timestampInMillis = timestamp.toLong() * 1000
    val date = Date(timestampInMillis)
    return simpleDateFormat.format(date)
}