package com.hackaprende.botia.core.api.dto

import com.hackaprende.botia.core.model.User

class UserDTOMapper {
    fun fromUserDTOToUserDomain(userDTO: UserDTO) =
        User(userDTO.id, userDTO.authenticationToken, userDTO.email)
}