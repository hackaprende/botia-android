package app.botia.android.customers.model

data class CustomerMessage(
    val customerAnswer: String,
    val userAnswer: String,
    val messageId: String,
    val timestamp: Long,
    val customerPhone: String,
    val messagingProduct: String, // whatsapp, facebook or instagram
    val functionName: String?, // The name of the ChatGPT function triggered to answer this
    val functionCall: String?, // The function to be called for the customer answer (if any)
    val sender: Int = SENDER_OTHER, // Who sent the message
) {
    companion object {
        // Who sent the message: Possible values
        const val SENDER_OTHER = 0 // Just a default value to prevent migration problems
        const val SENDER_CUSTOMER_BOT = 1 // The Customer sent the message and the bot answered
        const val SENDER_CUSTOMER = 2 // The Customer sent the message with bot disabled
        const val SENDER_USER = 3 // The User (Company) sent the message
    }
}

