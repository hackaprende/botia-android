package app.botia.android.customers.api.dto

import com.squareup.moshi.Json

data class SendMessageErrorDTO(
    val message: String,
    val type: String,
    val code: Int,
    @field:Json(name = "error_subcode") val errorSubcode: Int,
    @field:Json(name = "fbtrace_id") val facebookTraceId: String,
)