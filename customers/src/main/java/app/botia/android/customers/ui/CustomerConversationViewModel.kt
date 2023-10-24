package app.botia.android.customers.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.botia.android.core.api.ApiResponseStatus
import app.botia.android.customers.model.Customer
import app.botia.android.customers.model.CustomerMessage
import app.botia.android.customers.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CustomerConversationScreenState(
    val status: ApiResponseStatus<Any?>,
    val customer: Customer?,
)

private val initialState = CustomerConversationScreenState(
    status = ApiResponseStatus.None(),
    customer = null,
)

@HiltViewModel
class CustomerConversationViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val mutableStateFlow = MutableStateFlow(initialState)
    val state = mutableStateFlow.asStateFlow()

    init {
        downloadCustomerConversation()
    }

    private fun downloadCustomerConversation() {
        // TODO - Handle errors if null
        val customerId =
            savedStateHandle.get<Int>(CustomerConversationActivity.CUSTOMER_ID_KEY) ?: return
        val companyId =
            savedStateHandle.get<Int>(CustomerConversationActivity.COMPANY_ID_KEY) ?: return
        customerRepository
            .getCustomerConversation(companyId, customerId)
            .onEach(::handleCustomerConversationResponse)
            .launchIn(viewModelScope)
    }

    private suspend fun handleCustomerConversationResponse(
        customerConversationResponseStatus: ApiResponseStatus<Customer>
    ) {
        val newState = if (customerConversationResponseStatus is ApiResponseStatus.Success) {
            state.value.copy(
                status = customerConversationResponseStatus,
                customer = customerConversationResponseStatus.data
            )
        } else {
            state.value.copy(
                status = customerConversationResponseStatus,
            )
        }

        mutableStateFlow.emit(newState)
    }

    fun resetApiResponseStatus() {
        viewModelScope.launch {
            mutableStateFlow.emit(
                state.value.copy(
                    status = ApiResponseStatus.None(),
                )
            )
        }
    }
}