package app.botia.android.auth.repository

import app.botia.android.core.api.ApiResponseStatus
import app.botia.android.core.api.ApiService
import app.botia.android.core.api.Network
import app.botia.android.core.api.dto.LoginDTO
import app.botia.android.core.api.dto.SignUpDTO
import app.botia.android.core.api.dto.UserDTOMapper
import app.botia.android.core.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface AuthRepository {
    fun login(username: String, password: String): Flow<ApiResponseStatus<User>>
    fun signUp(username: String, password: String, firstName: String, lastName: String): Flow<ApiResponseStatus<User>>
}

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val network: Network,
) : app.botia.android.auth.repository.AuthRepository {
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

    override fun signUp(
        username: String,
        password: String,
        firstName: String,
        lastName: String,
    ) = network.makeNetworkCall {
        val signUpDTO = SignUpDTO(username, password, firstName, lastName)
        val signUpResponse = apiService.signUp(signUpDTO)

        if (!signUpResponse.isSuccess) {
            throw Exception(signUpResponse.message)
        }

        val userDTO = signUpResponse.data.userDTO
        val userDTOMapper = UserDTOMapper()
        userDTOMapper.fromUserDTOToUserDomain(userDTO)
    }
}