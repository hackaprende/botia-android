package com.epsilon.botia.customers.api.responses

import com.epsilon.botia.customers.api.dto.CustomerDTO
import com.squareup.moshi.Json

class CustomersResponse(
    @field:Json(name = "response_status") val status: String,
    val customers: List<CustomerDTO>
)