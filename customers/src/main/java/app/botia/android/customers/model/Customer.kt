package app.botia.android.customers.model

import app.botia.android.customers.utils.LAST_INTERACTION_DATE_FORMAT
import app.botia.android.customers.utils.LAST_INTERACTION_HOUR_FORMAT
import app.botia.android.customers.utils.getTimeWithFormat
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
    val messages: List<CustomerMessage> = listOf(),
) : Comparable<Customer> {

    val lastInteractionDate = getTimeWithFormat(
        LAST_INTERACTION_DATE_FORMAT,
        lastInteractionTimestamp,
    )

    val lastInteractionHour = getTimeWithFormat(
        LAST_INTERACTION_HOUR_FORMAT,
        lastInteractionTimestamp,
    )

    override fun compareTo(other: Customer) =
        if (lastInteractionTimestamp >= other.lastInteractionTimestamp) -1 else 1
}

