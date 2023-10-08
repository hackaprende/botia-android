package app.botia.android.core.api.dto

import app.botia.android.core.model.User

class UserDTOMapper {
    fun fromUserDTOToUserDomain(userDTO: UserDTO) =
        User(userDTO.id, userDTO.authenticationToken, userDTO.email)
}