package app.botia.android.core.api

import app.botia.android.core.AUTHENTICATION_ENDPOINT
import app.botia.android.core.api.dto.LoginDTO
import app.botia.android.core.api.dto.SignUpDTO
import app.botia.android.core.api.responses.AuthApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST(AUTHENTICATION_ENDPOINT)
    suspend fun login(@Body loginDTO: LoginDTO): AuthApiResponse

    @POST(AUTHENTICATION_ENDPOINT)
    suspend fun signUp(@Body signUpDTO: SignUpDTO): AuthApiResponse
}
