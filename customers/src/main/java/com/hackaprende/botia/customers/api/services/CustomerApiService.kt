package com.hackaprende.botia.customers.api.services

import com.hackaprende.botia.core.COMPANY_CUSTOMERS_ENDPOINT
import com.hackaprende.botia.customers.api.responses.CustomersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CustomerApiService {

    @GET(COMPANY_CUSTOMERS_ENDPOINT)
    suspend fun getCompanyCustomers(@Query("company_id") companyId: Int): CustomersResponse
}