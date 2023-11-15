package app.botia.android.core.api.dto

import com.squareup.moshi.Json

class LoginDTO(
    val username: String,
    val password: String,
    @field:Json(name = "fcm_token") val fcmToken: String,
)
