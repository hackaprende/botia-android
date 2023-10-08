package app.botia.android.core.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import app.botia.android.core.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface SessionManager {
    suspend fun storeUser(user: User)

    suspend fun logout()

    fun userTokenFlow(): Flow<String>
    fun userIdFlow(): Flow<Int>
}

val USER_ID_KEY = intPreferencesKey("user_id")
val USER_TOKEN_KEY = stringPreferencesKey("user_auth_token")
val USER_EMAIL_KEY = stringPreferencesKey("user_email")

private val Context.dataStore: DataStore<Preferences> by
preferencesDataStore(name = "user_settings")
class SessionManagerImpl@Inject constructor(
    @ApplicationContext context: Context) : SessionManager {

    private val dataStore = context.dataStore
    override suspend fun storeUser(user: User) {
        dataStore.edit {
            it[USER_ID_KEY] = user.id
            it[USER_TOKEN_KEY] = user.authenticationToken
            it[USER_EMAIL_KEY] = user.email
        }
    }

    override suspend fun logout() {
        dataStore.edit {
            it[USER_ID_KEY] = -1
            it[USER_TOKEN_KEY] = ""
            it[USER_EMAIL_KEY] = ""
        }
    }

    override fun userTokenFlow(): Flow<String> = dataStore.data.map {
        it[USER_TOKEN_KEY] ?: ""
    }

    override fun userIdFlow(): Flow<Int> = dataStore.data.map {
        it[USER_ID_KEY] ?: 0
    }
}