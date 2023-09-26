package com.hackaprende.botia.core.api.responses

import com.hackaprende.botia.core.api.dto.UserDTO
import com.squareup.moshi.Json

class UserResponse(@field:Json(name = "user") val userDTO: UserDTO)