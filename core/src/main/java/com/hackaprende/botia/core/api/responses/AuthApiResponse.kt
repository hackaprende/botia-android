package com.hackaprende.botia.core.api.responses

import com.squareup.moshi.Json

class AuthApiResponse(
    @field:Json(name = "is_success") val isSuccess: Boolean,
    val message: String,
    val data: UserResponse,
)
