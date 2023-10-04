package com.epsilon.botia.core.api.dto

import com.epsilon.botia.core.model.User

class UserDTOMapper {
    fun fromUserDTOToUserDomain(userDTO: UserDTO) =
        User(userDTO.id, userDTO.authenticationToken, userDTO.email)
}