package app.botia.android.customers.model

import app.botia.android.customers.utils.LAST_INTERACTION_DATE_FORMAT
import app.botia.android.customers.utils.LAST_INTERACTION_HOUR_FORMAT
import app.botia.android.customers.utils.getTimeWithFormat
import kotlin.math.abs
import java.util.Collections

private const val ONE_DAY_IN_SECONDS = 60 * 60 * 24

class Customer(
    val id: Int,
    val name: String,
    val phoneNumber: String,
    private val lastInteractionTimestamp: Int,
    val isBotEnabled: Boolean,
    val needCustomAttention: Boolean,
    val messages: List<CustomerMessage> = listOf(),
) : Comparable<Customer> {

    init {
        Collections.sort(messages)
    }

    val lastInteractionDate = getTimeWithFormat(
        LAST_INTERACTION_DATE_FORMAT,
        lastInteractionTimestamp,
    )

    val lastInteractionHour = getTimeWithFormat(
        LAST_INTERACTION_HOUR_FORMAT,
        lastInteractionTimestamp,
    )

    val lastInteractionOlderThanOneDay: Boolean
        get() {
            val currentTimeMillis = System.currentTimeMillis() / 1000
            val lastInteractionTimestamp1 = lastInteractionTimestamp
            val difference = currentTimeMillis - lastInteractionTimestamp1
            val oneDayInSeconds = ONE_DAY_IN_SECONDS
            return abs(difference) > (oneDayInSeconds)
        }


    override fun compareTo(other: Customer) =
        if (lastInteractionTimestamp >= other.lastInteractionTimestamp) -1 else 1
}

