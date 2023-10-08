package app.botia.android.customers.api.services

import app.botia.android.core.USER_COMPANY_ENDPOINT
import app.botia.android.core.api.ApiServiceInterceptor
import app.botia.android.customers.api.responses.UserCompanyResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface CompanyApiService {
    @Headers("${ApiServiceInterceptor.NEEDS_AUTH_HEADER_KEY}: true")
    @GET(USER_COMPANY_ENDPOINT)
    suspend fun getUserCompany(): UserCompanyResponse
}