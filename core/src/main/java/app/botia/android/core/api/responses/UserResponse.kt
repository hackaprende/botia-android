package app.botia.android.core.api.responses

import app.botia.android.core.api.dto.UserDTO
import com.squareup.moshi.Json

class UserResponse(@field:Json(name = "user") val userDTO: UserDTO)