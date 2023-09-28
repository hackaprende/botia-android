package com.hackaprende.botia.customers.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackaprende.botia.core.api.ApiResponseStatus
import com.hackaprende.botia.core.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class CustomersScreenState(
    val status: ApiResponseStatus<Any?>,
    val isUserLoggedIn: Boolean,
)

private val initialState = CustomersScreenState(
    status = ApiResponseStatus.None(),
    isUserLoggedIn = true,
)
@HiltViewModel
class CustomersViewModel @Inject constructor(
    private val sessionManager: SessionManager,
) : ViewModel() {
    private val stateFlow = MutableStateFlow(initialState)
    val state = stateFlow.asStateFlow()

    init {
        isUserLoggedIn()
    }

    private fun isUserLoggedIn() {
        sessionManager
            .userTokenFlow()
            .onEach {
                authenticationToken ->
                stateFlow.emit(
                    state.value.copy(
                        isUserLoggedIn = authenticationToken.isNotEmpty()
                    )
                )
            }
            .launchIn(viewModelScope)

    }
}