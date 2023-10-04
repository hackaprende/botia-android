package com.epsilon.botia.customers.api.services

import com.epsilon.botia.core.COMPANY_CUSTOMERS_ENDPOINT
import com.epsilon.botia.core.UPDATE_CUSTOMER_ENDPOINT
import com.epsilon.botia.core.api.ApiServiceInterceptor
import com.epsilon.botia.customers.api.requests.ToggleBotEnabledRequest
import com.epsilon.botia.customers.api.requests.TurnOffNeedCustomAttentionRequest
import com.epsilon.botia.customers.api.responses.CustomerResponse
import com.epsilon.botia.customers.api.responses.CustomersResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path

interface CustomerApiService {
    @Headers("${ApiServiceInterceptor.NEEDS_AUTH_HEADER_KEY}: true")
    @GET(COMPANY_CUSTOMERS_ENDPOINT)
    suspend fun getCompanyCustomers(@Path("company_id") companyId: Int): CustomersResponse

    @Headers("${ApiServiceInterceptor.NEEDS_AUTH_HEADER_KEY}: true")
    @PUT(UPDATE_CUSTOMER_ENDPOINT)
    suspend fun toggleBotEnabledForCustomer(
        @Path("customer_id") customerId: Int,
        @Body toggleBotEnabledRequest: ToggleBotEnabledRequest,
    ): CustomerResponse

    @Headers("${ApiServiceInterceptor.NEEDS_AUTH_HEADER_KEY}: true")
    @PUT(UPDATE_CUSTOMER_ENDPOINT)
    suspend fun turnOffNeedCustomerAttentionForCustomer(
        @Path("customer_id") customerId: Int,
        @Body turnOffNeedCustomAttentionRequest: TurnOffNeedCustomAttentionRequest,
    ): CustomerResponse
}