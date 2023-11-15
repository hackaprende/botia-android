package app.botia.android.core.api.dto

import com.squareup.moshi.Json

class SignUpDTO(
    val username: String,
    val password: String,
    @field:Json(name = "first_name") val firstName: String,
    @field:Json(name = "last_name") val lastName: String,
    @field:Json(name = "fcm_token") val fcmToken: String,
)
