package app.botia.android.customers.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Customer(
    val id: Int,
    val name: String,
    val phoneNumber: String,
    private val lastInteractionTimestamp: Int,
    val isBotEnabled: Boolean,
    val needCustomAttention: Boolean,
) : Comparable<Customer> {
    companion object {
        private const val LAST_INTERACTION_DATE_FORMAT = "yyyy-MMM-dd"
        private const val LAST_INTERACTION_HOUR_FORMAT = "hh:mm aa"
    }

    val lastInteractionDate = getTimeWithFormat(LAST_INTERACTION_DATE_FORMAT)
    val lastInteractionHour = getTimeWithFormat(LAST_INTERACTION_HOUR_FORMAT)

    private fun getTimeWithFormat(format: String): String {
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        val timestampInMillis = lastInteractionTimestamp.toLong() * 1000
        val date = Date(timestampInMillis)
        return simpleDateFormat.format(date)
    }

    override fun compareTo(other: Customer) =
        if (lastInteractionTimestamp >= other.lastInteractionTimestamp) -1 else 1
}

