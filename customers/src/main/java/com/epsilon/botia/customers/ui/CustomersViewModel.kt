package com.epsilon.botia.customers.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epsilon.botia.core.api.ApiResponseStatus
import com.epsilon.botia.core.api.ApiServiceInterceptorHandler
import com.epsilon.botia.customers.model.Company
import com.epsilon.botia.core.util.SessionManager
import com.epsilon.botia.customers.model.Customer
import com.epsilon.botia.customers.repository.CompanyRepository
import com.epsilon.botia.customers.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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

    fun toggleBotEnabledForCustomer(customer: Customer) {
        customerRepository
            .toggleBotEnabledForCustomer(customer.id, !customer.isBotEnabled)
            .onEach(::handleToggleBotEnabledForCustomerResponse)
            .launchIn(viewModelScope)
    }

    private suspend fun handleToggleBotEnabledForCustomerResponse(
        apiResponseStatus: ApiResponseStatus<Unit>
    ) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
            // Customers are not visible if no company, so company cannot be null here
            getCompanyCustomers(state.value.company!!.id)
        } else {
            val newState = state.value.copy(
                status = apiResponseStatus,
            )
            stateFlow.emit(newState)
        }
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
                status = apiResponseStatus,
                company = company,
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
            // We are sorting the customers from recent to older, for more details check
            // Customer class Comparable.
            val sortedCustomers = customers.sorted()
            state.value.copy(
                status = apiResponseStatus,
                customers = sortedCustomers
            )
        } else {
            state.value.copy(
                status = apiResponseStatus,
            )
        }

        stateFlow.emit(newState)
    }

    fun resetApiResponseStatus() {
        viewModelScope.launch {
            stateFlow.emit(
                state.value.copy(
                    status = ApiResponseStatus.None(),
                )
            )
        }
    }
}