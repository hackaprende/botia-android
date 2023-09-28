package com.hackaprende.botia.core.di

import com.hackaprende.botia.core.util.SessionManager
import com.hackaprende.botia.core.util.SessionManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SessionManagerModule {
    @Binds
    abstract fun bindNetwork(sessionManagerImpl: SessionManagerImpl): SessionManager
}
