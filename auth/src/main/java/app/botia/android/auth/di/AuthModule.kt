package app.botia.android.auth.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: app.botia.android.auth.repository.AuthRepositoryImpl
    ): app.botia.android.auth.repository.AuthRepository
}