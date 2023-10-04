package com.epsilon.botia.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epsilon.botia.auth.R
import com.epsilon.botia.auth.repository.AuthRepository
import com.epsilon.botia.core.api.ApiResponseStatus
import com.epsilon.botia.core.model.User
import com.epsilon.botia.core.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthScreenState(
    val status: ApiResponseStatus<Any?>,
    val authenticationToken: String?,
    val emailFieldError: Int?,
    val firstNameFieldError: Int?,
    val lastNameFieldError: Int?,
    val passwordFieldError: Int?,
    val confirmPasswordFieldError: Int?,
)

private val initialState = AuthScreenState(
    status = ApiResponseStatus.None(),
    authenticationToken = null,
    emailFieldError = null,
    firstNameFieldError = null,
    lastNameFieldError = null,
    passwordFieldError = null,
    confirmPasswordFieldError = null,
)
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager,
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

    fun signUp(
        username: String,
        password: String,
        passwordConfirmation: String,
        firstName: String,
        lastName: String,
    ) {
        viewModelScope.launch {
            val newState: AuthScreenState = when {
                username.isEmpty() -> state.value.copy(
                    emailFieldError = R.string.email_is_not_valid,
                )

                firstName.isEmpty() -> state.value.copy(
                    firstNameFieldError = R.string.field_must_not_be_empty,
                )

                lastName.isEmpty() -> state.value.copy(
                    lastNameFieldError = R.string.field_must_not_be_empty,
                )

                password.isEmpty() -> state.value.copy(
                    passwordFieldError = R.string.password_must_not_be_empty,
                )

                passwordConfirmation.isEmpty() -> state.value.copy(
                    confirmPasswordFieldError = R.string.password_must_not_be_empty,
                )

                password != passwordConfirmation -> {
                    state.value.copy(
                        passwordFieldError = R.string.passwords_do_not_match,
                        confirmPasswordFieldError = R.string.passwords_do_not_match,
                    )
                }

                else -> {
                    authRepository
                        .signUp(username, password, firstName, lastName)
                        .onEach (::processLoginResult)
                        .launchIn(viewModelScope)
                    state.value
                }
            }

            stateFlow.emit(newState)
        }
    }

    private suspend fun processLoginResult(apiResponseStatus: ApiResponseStatus<User>) {
        val newState = if (apiResponseStatus is ApiResponseStatus.Success) {
            val user = apiResponseStatus.data
            sessionManager.storeUser(user)
            state.value.copy(
                status = apiResponseStatus,
                authenticationToken = user.authenticationToken
            )
        } else {
            state.value.copy(
                status = apiResponseStatus,
            )
        }

        stateFlow.emit(newState)
    }
}