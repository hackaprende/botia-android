package com.hackaprende.botia.core.responses

import com.squareup.moshi.Json

class AuthApiResponse(
    @field:Json(name = "user_id") val userId: Int,
    val token: String,
    val email: String,
)
