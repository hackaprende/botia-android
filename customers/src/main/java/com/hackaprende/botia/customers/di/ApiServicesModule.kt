package com.hackaprende.botia.customers.di

import com.hackaprende.botia.customers.api.CompanyApiService
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
}