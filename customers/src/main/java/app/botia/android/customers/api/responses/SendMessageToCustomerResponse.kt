package app.botia.android.customers.api.responses

import app.botia.android.customers.api.dto.SendMessageErrorDTO
import com.squareup.moshi.Json

class SendMessageToCustomerResponse(
    @field:Json(name = "response_status") val status: String,
    val error: SendMessageErrorDTO?,
)
