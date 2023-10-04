package com.epsilon.botia.customers.api.responses

import com.epsilon.botia.customers.api.dto.CompanyDTO
import com.squareup.moshi.Json

class UserCompanyResponse(
    @field:Json(name = "response_status") val responseStatus: String,
    val company: CompanyDTO,
)