package app.botia.android.customers.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.botia.android.core.api.ApiResponseStatus
import app.botia.android.core.api.ApiServiceInterceptorHandler
import app.botia.android.core.util.SessionManager
import app.botia.android.customers.model.Company
import app.botia.android.customers.model.Customer
import app.botia.android.customers.repository.CompanyRepository
import app.botia.android.customers.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

    private var job = Job()
    private val backgroundScope = CoroutineScope(Dispatchers.IO + job)

    init {
        isUserLoggedIn()
    }

    fun toggleNeedCustomAttentionForCustomer(customer: Customer) {
        if (customer.needCustomAttention) {
            // We need to process the result on a backgroundScope to run it when Whatsapp is opened
            // and the app goes to background.
            customerRepository
                .turnOffNeedCustomerAttentionForCustomer(customer.id)
                .onEach(::handleCustomerUpdatedResult)
                .launchIn(backgroundScope)
        }
    }

    fun toggleBotEnabledForCustomer(customer: Customer) {
        customerRepository
            .toggleBotEnabledForCustomer(customer.id, !customer.isBotEnabled)
            .onEach(::handleCustomerUpdatedResult)
            .launchIn(viewModelScope)
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.logout()
            stateFlow.emit(
                state.value.copy(
                    isUserLoggedIn = false
                )
            )
        }
    }

    private suspend fun handleCustomerUpdatedResult(
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