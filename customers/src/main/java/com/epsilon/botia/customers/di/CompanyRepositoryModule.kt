package com.epsilon.botia.customers.di

import com.epsilon.botia.customers.repository.CompanyRepository
import com.epsilon.botia.customers.repository.CompanyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class CompanyRepositoryModule {

    @Binds
    abstract fun bindCompanyRepository(
        companyRepositoryImpl: CompanyRepositoryImpl
    ): CompanyRepository
}