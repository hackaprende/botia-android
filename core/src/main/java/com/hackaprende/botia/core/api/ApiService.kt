package com.hackaprende.botia.core.api

import com.hackaprende.botia.core.AUTHENTICATION_ENDPOINT
import com.hackaprende.botia.core.api.dto.LoginDTO
import com.hackaprende.botia.core.api.dto.SignUpDTO
import com.hackaprende.botia.core.api.responses.AuthApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST(AUTHENTICATION_ENDPOINT)
    suspend fun login(@Body loginDTO: LoginDTO): AuthApiResponse

    @POST(AUTHENTICATION_ENDPOINT)
    suspend fun signUp(@Body signUpDTO: SignUpDTO): AuthApiResponse

//    @GET(COMPANY_CUSTOMERS_ENDPOINT)
//    suspend fun getCompanyCustomers(@Query("company_id") companyId: Int): DogApiResponse
}
