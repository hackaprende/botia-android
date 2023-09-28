package com.hackaprende.botia.customers.api.services

import com.hackaprende.botia.core.USER_COMPANY_ENDPOINT
import com.hackaprende.botia.core.api.ApiServiceInterceptor
import com.hackaprende.botia.customers.api.responses.UserCompanyResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface CompanyApiService {
    @Headers("${ApiServiceInterceptor.NEEDS_AUTH_HEADER_KEY}: true")
    @GET(USER_COMPANY_ENDPOINT)
    suspend fun getUserCompany(): UserCompanyResponse
}