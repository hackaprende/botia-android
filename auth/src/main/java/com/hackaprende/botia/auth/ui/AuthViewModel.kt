package com.hackaprende.botia.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackaprende.botia.auth.repository.AuthRepository
import com.hackaprende.botia.core.api.ApiResponseStatus
import com.hackaprende.botia.core.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthScreenState(
    val status: ApiResponseStatus<Any?>,
    val user: User?,
    val emailFieldError: Int?,
    val passwordFieldError: Int?,
    val confirmPasswordFieldError: Int?,
)

private val initialState = AuthScreenState(
    status = ApiResponseStatus.None(),
    user = null,
    emailFieldError = null,
    passwordFieldError = null,
    confirmPasswordFieldError = null,
)
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val stateFlow = MutableStateFlow(initialState)
    val state = stateFlow.asStateFlow()


    fun resetApiResponseStatus() {
        viewModelScope.launch {
            stateFlow.emit(initialState)
        }
    }

    fun resetFieldErrors() {
        viewModelScope.launch {
            stateFlow.emit(
                state.value.copy(
                    emailFieldError = null,
                    passwordFieldError = null,
                    confirmPasswordFieldError = null,
                )
            )
        }
    }

    fun login(username: String, password: String) {
        authRepository
            .login(username, password)
            .onEach (::processLoginResult)
            .launchIn(viewModelScope)
    }

    private suspend fun processLoginResult(apiResponseStatus: ApiResponseStatus<User>) {
        val newState = if (apiResponseStatus is ApiResponseStatus.Success) {
            state.value.copy(
                status = apiResponseStatus,
                user = apiResponseStatus.data
            )
        } else {
            state.value.copy(
                status = apiResponseStatus,
            )
        }

        stateFlow.emit(newState)
    }
}