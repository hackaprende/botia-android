package app.botia.android.customers.model

data class CustomerMessage(
    val customerAnswer: String,
    val userAnswer: String,
    val messageId: String,
    val timestamp: Long,
    val customerPhone: String,
    val messagingProduct: String, // whatsapp, facebook or instagram
    val functionName: String?, // The name of the ChatGPT function triggered to answer this
    val functionCall: String? // The function to be called for the customer answer (if any)
)

