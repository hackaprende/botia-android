package app.botia.android.core.di

import app.botia.android.core.api.Network
import app.botia.android.core.api.NetworkImpl
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
