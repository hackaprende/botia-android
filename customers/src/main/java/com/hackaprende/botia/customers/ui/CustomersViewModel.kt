package com.hackaprende.botia.customers.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackaprende.botia.core.api.ApiResponseStatus
import com.hackaprende.botia.core.api.ApiServiceInterceptorHandler
import com.hackaprende.botia.customers.model.Company
import com.hackaprende.botia.core.util.SessionManager
import com.hackaprende.botia.customers.model.Customer
import com.hackaprende.botia.customers.repository.CompanyRepository
import com.hackaprende.botia.customers.repository.CustomerRepository
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
    val customers: List<Customer>
)

private val initialState = CustomersScreenState(
    status = ApiResponseStatus.None(),
    isUserLoggedIn = true,
    company = null,
    customers = listOf(),
)
@HiltViewModel
class CustomersViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val companyRepository: CompanyRepository,
    private val customerRepository: CustomerRepository,
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
            val company = apiResponseStatus.data
            getCompanyCustomers(company.id)
            state.value.copy(
                company = company
            )
        } else {
            state.value.copy(
                status = apiResponseStatus,
            )
        }

        stateFlow.emit(newState)
    }

    private fun getCompanyCustomers(companyId: Int) {
        customerRepository
            .getCompanyCustomers(companyId)
            .onEach(::handleCustomersResponseStatus)
            .launchIn(viewModelScope)
    }

    private suspend fun handleCustomersResponseStatus(
        apiResponseStatus: ApiResponseStatus<List<Customer>>
    ) {
        val newState = if (apiResponseStatus is ApiResponseStatus.Success) {
            val customers = apiResponseStatus.data
            state.value.copy(
                customers = customers
            )
        } else {
            state.value.copy(
                status = apiResponseStatus,
            )
        }

        stateFlow.emit(newState)
    }
}