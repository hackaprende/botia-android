package app.botia.android.core.di

import app.botia.android.core.util.SessionManager
import app.botia.android.core.util.SessionManagerImpl
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
