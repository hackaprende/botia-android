package com.hackaprende.botia.core.api

import com.hackaprende.botia.core.AUTHENTICATION_ENDPOINT
import com.hackaprende.botia.core.COMPANY_CUSTOMERS_ENDPOINT
import com.hackaprende.botia.core.USER_COMPANY_ENDPOINT
import com.hackaprende.botia.core.api.dto.LoginDTO
import com.hackaprende.botia.core.api.dto.SignUpDTO
import com.hackaprende.botia.core.api.responses.AuthApiResponse
import com.hackaprende.botia.core.api.responses.UserCompanyResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST(AUTHENTICATION_ENDPOINT)
    suspend fun login(@Body loginDTO: LoginDTO): AuthApiResponse

    @POST(AUTHENTICATION_ENDPOINT)
    suspend fun signUp(@Body signUpDTO: SignUpDTO): AuthApiResponse

    @Headers("${ApiServiceInterceptor.NEEDS_AUTH_HEADER_KEY}: true")
    @GET(USER_COMPANY_ENDPOINT)
    suspend fun getUserCompany(): UserCompanyResponse

//    @GET(COMPANY_CUSTOMERS_ENDPOINT)
//    suspend fun getCompanyCustomers(@Query("company_id") companyId: Int): DogApiResponse
}
