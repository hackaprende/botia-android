package app.botia.android.customers.di

import app.botia.android.customers.api.services.CompanyApiService
import app.botia.android.customers.api.services.CustomerApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class ApiServicesModule {
    @Provides
    fun providesCompanyApiService(
        retrofit: Retrofit
    ): CompanyApiService = retrofit.create(CompanyApiService::class.java)

    @Provides
    fun providesCustomerApiService(
        retrofit: Retrofit
    ): CustomerApiService = retrofit.create(CustomerApiService::class.java)
}