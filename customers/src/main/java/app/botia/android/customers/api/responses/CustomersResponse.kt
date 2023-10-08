package app.botia.android.customers.api.responses

import app.botia.android.customers.api.dto.CustomerDTO
import com.squareup.moshi.Json

class CustomersResponse(
    @field:Json(name = "response_status") val status: String,
    val customers: List<CustomerDTO>
)