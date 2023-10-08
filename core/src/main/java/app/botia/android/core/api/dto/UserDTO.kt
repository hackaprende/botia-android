package app.botia.android.core.api.dto

import com.squareup.moshi.Json

class UserDTO(
    @field:Json(name = "user_id") val id: Int,
    @field:Json(name = "token") val authenticationToken: String,
    val email: String,
)