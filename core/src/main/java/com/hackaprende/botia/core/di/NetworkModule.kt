package com.hackaprende.botia.core.di

import com.hackaprende.botia.core.api.Network
import com.hackaprende.botia.core.api.NetworkImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {
    @Binds
    abstract fun bindNetwork(networkImpl: NetworkImpl): Network
}
