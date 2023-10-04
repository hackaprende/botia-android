package com.epsilon.botia.core.api.responses

import com.epsilon.botia.core.api.dto.UserDTO
import com.squareup.moshi.Json

class UserResponse(@field:Json(name = "user") val userDTO: UserDTO)