package app.botia.android.customers.api.responses

import app.botia.android.customers.api.dto.CustomerDTO
import app.botia.android.customers.api.dto.CustomerMessageDTO
import com.squareup.moshi.Json

data class CustomerMessageListResponse(
    @field:Json(name = "response_status") val status: String,
    val customer: CustomerDTO,
    @field:Json(name = "customer_messages") val customerMessages: List<CustomerMessageDTO>
)