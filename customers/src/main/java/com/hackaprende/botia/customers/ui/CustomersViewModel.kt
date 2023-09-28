package com.hackaprende.botia.customers.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackaprende.botia.core.api.ApiResponseStatus
import com.hackaprende.botia.core.api.ApiServiceInterceptorHandler
import com.hackaprende.botia.core.model.Company
import com.hackaprende.botia.core.util.SessionManager
import com.hackaprende.botia.customers.repository.CompanyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class CustomersScreenState(
    val status: ApiResponseStatus<Any?>,
    val isUserLoggedIn: Boolean,
    val company: Company?,
)

private val initialState = CustomersScreenState(
    status = ApiResponseStatus.None(),
    isUserLoggedIn = true,
    company = null,
)
@HiltViewModel
class CustomersViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val companyRepository: CompanyRepository
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
                val userLoggedIn = authenticationToken.isNotEmpty()
                stateFlow.emit(
                    state.value.copy(
                        isUserLoggedIn = userLoggedIn
                    )
                )

                if (userLoggedIn) {
                    // Set the token to the interceptor so it's added in the headers
                    // of any request that needs authentication.
                    ApiServiceInterceptorHandler.setSessionToken(authenticationToken)
                    // Now that we know the user is logged in, get the company.
                    getUserCompany()
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getUserCompany() {
        companyRepository
            .getUserCompany()
            .onEach(::handleUserCompanyResponseStatus)
            .launchIn(viewModelScope)
    }

    private suspend fun handleUserCompanyResponseStatus(apiResponseStatus: ApiResponseStatus<Company>) {
        val newState = if (apiResponseStatus is ApiResponseStatus.Success) {
            state.value.copy(
                company = apiResponseStatus.data
            )
        } else {
            state.value.copy(
                status = apiResponseStatus,
            )
        }

        stateFlow.emit(newState)
    }
}