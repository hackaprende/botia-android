package app.botia.android.customers.model

data class SendMessageError(
    val message: String,
    val type: String,
    val code: Int,
    val errorSubcode: Int,
    val facebookTraceId: String,
)