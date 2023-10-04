package com.epsilon.botia.core.api

import com.epsilon.botia.core.AUTHENTICATION_ENDPOINT
import com.epsilon.botia.core.api.dto.LoginDTO
import com.epsilon.botia.core.api.dto.SignUpDTO
import com.epsilon.botia.core.api.responses.AuthApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST(AUTHENTICATION_ENDPOINT)
    suspend fun login(@Body loginDTO: LoginDTO): AuthApiResponse

    @POST(AUTHENTICATION_ENDPOINT)
    suspend fun signUp(@Body signUpDTO: SignUpDTO): AuthApiResponse
}
