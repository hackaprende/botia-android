package com.hackaprende.botia.customers.api.dto

import com.squareup.moshi.Json

class CompanyDTO(
    val id: Int,
    @field:Json(name = "phone_number") val phoneNumber: String,
    val timezone: String,
)
