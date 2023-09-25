package com.hackaprende.botia.core.dto

import com.squareup.moshi.Json

class SignUpDTO(
    val username: String,
    val password: String,
    @field:Json(name = "first_name") val firstName: Int,
    @field:Json(name = "last_name") val lastName: Int,
)
