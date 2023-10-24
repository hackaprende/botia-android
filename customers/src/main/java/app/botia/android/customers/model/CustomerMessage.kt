package app.botia.android.customers.model

import app.botia.android.customers.utils.LAST_INTERACTION_DATE_FORMAT
import app.botia.android.customers.utils.LAST_INTERACTION_HOUR_FORMAT
import app.botia.android.customers.utils.getTimeWithFormat

data class CustomerMessage(
    val customerAnswer: String,
    val userAnswer: String,
    val messageId: String,
    val timestamp: Int,
    val customerPhone: String,
    val messagingProduct: String, // whatsapp, facebook or instagram
    val functionName: String?, // The name of the ChatGPT function triggered to answer this
    val functionCall: String?, // The function to be called for the customer answer (if any)
    val sender: Int = SENDER_OTHER, // Who sent the message
) : Comparable<CustomerMessage> {
    companion object {
        // Who sent the message: Possible values
        const val SENDER_OTHER = 0 // Just a default value to prevent migration problems
        const val SENDER_CUSTOMER_BOT = 1 // The Customer sent the message and the bot answered
        const val SENDER_CUSTOMER = 2 // The Customer sent the message with bot disabled
        const val SENDER_USER = 3 // The User (Company) sent the message
    }

    val lastInteractionDate = getTimeWithFormat(
        LAST_INTERACTION_DATE_FORMAT,
        timestamp,
    )

    val lastInteractionHour = getTimeWithFormat(
        LAST_INTERACTION_HOUR_FORMAT,
        timestamp,
    )

    override fun compareTo(other: CustomerMessage) =
        if (timestamp >= other.timestamp) 1 else -1
}

