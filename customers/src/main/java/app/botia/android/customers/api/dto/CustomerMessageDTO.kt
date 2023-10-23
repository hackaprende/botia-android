package app.botia.android.customers.api.dto

import com.squareup.moshi.Json

data class CustomerMessageDTO(
    @field:Json(name = "received_message") val customerAnswer: String,
    @field:Json(name = "bot_answer") val userAnswer: String,
    @field:Json(name = "message_id") val messageId: String,
    val timestamp: Long,
    @field:Json(name = "customer_phone") val customerPhone: String,
    @field:Json(name = "messaging_product") val messagingProduct: String, // whatsapp, facebook or instagram
    @field:Json(name = "function_name") val functionName: String?, // The name of the ChatGPT function triggered to answer this
    @field:Json(name = "function_call") val functionCall: String? // The function to be called for the customer answer (if any)
)