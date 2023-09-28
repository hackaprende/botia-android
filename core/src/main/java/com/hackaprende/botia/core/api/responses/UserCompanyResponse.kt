package com.hackaprende.botia.core.api.responses

import com.hackaprende.botia.core.api.dto.CompanyDTO
import com.squareup.moshi.Json

class UserCompanyResponse(
    @field:Json(name = "response_status") val responseStatus: String,
    val company: CompanyDTO,
)