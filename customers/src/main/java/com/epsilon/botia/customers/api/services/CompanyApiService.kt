package com.epsilon.botia.customers.api.services

import com.epsilon.botia.core.USER_COMPANY_ENDPOINT
import com.epsilon.botia.core.api.ApiServiceInterceptor
import com.epsilon.botia.customers.api.responses.UserCompanyResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface CompanyApiService {
    @Headers("${ApiServiceInterceptor.NEEDS_AUTH_HEADER_KEY}: true")
    @GET(USER_COMPANY_ENDPOINT)
    suspend fun getUserCompany(): UserCompanyResponse
}