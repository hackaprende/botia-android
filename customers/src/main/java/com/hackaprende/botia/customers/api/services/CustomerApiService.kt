package com.hackaprende.botia.customers.api.services

import com.hackaprende.botia.core.COMPANY_CUSTOMERS_ENDPOINT
import com.hackaprende.botia.core.api.ApiServiceInterceptor
import com.hackaprende.botia.customers.api.responses.CustomersResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface CustomerApiService {
    @Headers("${ApiServiceInterceptor.NEEDS_AUTH_HEADER_KEY}: true")
    @GET(COMPANY_CUSTOMERS_ENDPOINT)
    suspend fun getCompanyCustomers(@Path("company_id") companyId: Int): CustomersResponse
}