package com.epsilon.botia.customers.di

import com.epsilon.botia.customers.repository.CustomerRepository
import com.epsilon.botia.customers.repository.CustomerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class CustomerRepositoryModule {

    @Binds
    abstract fun bindCustomerRepository(
        customerRepositoryImpl: CustomerRepositoryImpl
    ): CustomerRepository
}