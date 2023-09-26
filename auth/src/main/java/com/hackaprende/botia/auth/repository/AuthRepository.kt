package com.hackaprende.botia.auth.repository

import com.hackaprende.botia.core.api.ApiResponseStatus
import com.hackaprende.botia.core.api.ApiService
import com.hackaprende.botia.core.api.Network
import com.hackaprende.botia.core.api.dto.LoginDTO
import com.hackaprende.botia.core.api.dto.UserDTOMapper
import com.hackaprende.botia.core.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface AuthRepository {
    fun login(username: String, password: String): Flow<ApiResponseStatus<User>>
}

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val network: Network,
) : AuthRepository {
    override fun login(username: String, password: String) =
        network.makeNetworkCall {
            val loginDTO = LoginDTO(username, password)
            val loginResponse = apiService.login(loginDTO)

            if (!loginResponse.isSuccess) {
                throw Exception(loginResponse.message)
            }

            val userDTO = loginResponse.data.userDTO
            val userDTOMapper = UserDTOMapper()
            userDTOMapper.fromUserDTOToUserDomain(userDTO)
        }
}