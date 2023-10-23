package app.botia.android.customers.api.services

import app.botia.android.core.COMPANY_CUSTOMERS_ENDPOINT
import app.botia.android.core.CUSTOMER_MESSAGES_ENDPOINT
import app.botia.android.core.UPDATE_CUSTOMER_ENDPOINT
import app.botia.android.core.api.ApiServiceInterceptor
import app.botia.android.customers.api.requests.ToggleBotEnabledRequest
import app.botia.android.customers.api.requests.TurnOffNeedCustomAttentionRequest
import app.botia.android.customers.api.responses.CustomerMessageListResponse
import app.botia.android.customers.api.responses.CustomerResponse
import app.botia.android.customers.api.responses.CustomersResponse
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

    @Headers("${ApiServiceInterceptor.NEEDS_AUTH_HEADER_KEY}: true")
    @GET(CUSTOMER_MESSAGES_ENDPOINT)
    suspend fun getCustomerMessages(
        @Path("company_id") companyId: Int,
        @Path("customer_id") customerId: Int,
    ): CustomerMessageListResponse
}