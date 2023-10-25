package app.botia.android.customers.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.botia.android.core.api.ApiResponseStatus
import app.botia.android.customers.model.Customer
import app.botia.android.customers.model.SendMessageError
import app.botia.android.customers.model.SendMessageResult
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
    val sendMessageError: SendMessageError?,
    val messageToSend: String,
)

private val initialState = CustomerConversationScreenState(
    status = ApiResponseStatus.None(),
    customer = null,
    sendMessageError = null,
    messageToSend = "",
)

private const val ERROR_SENDING_MESSAGE_EXPIRED_TOKEN_CODE = 190
private const val ERROR_SENDING_MESSAGE_EXPIRED_TOKEN_SUB_CODE = 463
const val FACEBOOK_EXPIRED_TOKEN_ERROR = "facebook_expired_token_error"
const val SEND_MESSAGE_GENERIC_ERROR = "send_message_generic_error"

@HiltViewModel
class CustomerConversationViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val mutableStateFlow = MutableStateFlow(initialState)
    val state = mutableStateFlow.asStateFlow()

    private var companyId = savedStateHandle.get<Int>(CustomerConversationActivity.COMPANY_ID_KEY)!!
    private var customerId = savedStateHandle.get<Int>(CustomerConversationActivity.CUSTOMER_ID_KEY)!!

    init {
        downloadCustomerConversation()
    }

    fun sendMessageToCustomer() {
        customerRepository
            .sendMessageToCustomer(companyId, customerId, state.value.messageToSend)
            .onEach(::handleSendMessageResponse)
            .launchIn(viewModelScope)
    }

    fun updateMessageToSend(messageToSend: String) {
        viewModelScope.launch {
            val newState = state.value.copy(
                messageToSend = messageToSend,
            )
            mutableStateFlow.emit(newState)
        }
    }

    private suspend fun handleSendMessageResponse(
        apiResponseStatus: ApiResponseStatus<SendMessageResult>
    ) {
        // No need to handle loading here as we don't want to show LoadingWheel
        if (apiResponseStatus is ApiResponseStatus.Success) {
            val sendMessageError = apiResponseStatus.data.sendMessageError
            if (sendMessageError != null) {
                val errorCode = sendMessageError.code
                val errorSubCode = sendMessageError.errorSubcode
                val errorMessageId = if (errorCode == ERROR_SENDING_MESSAGE_EXPIRED_TOKEN_CODE &&
                    errorSubCode == ERROR_SENDING_MESSAGE_EXPIRED_TOKEN_SUB_CODE) {
                    FACEBOOK_EXPIRED_TOKEN_ERROR
                } else {
                    SEND_MESSAGE_GENERIC_ERROR
                }
                val newState = state.value.copy(
                    status = ApiResponseStatus.Error(errorMessageId),
                )
                mutableStateFlow.emit(newState)
            } else {
                // Refresh screen
                downloadCustomerConversation()
                updateMessageToSend("")
            }
        } else if (apiResponseStatus is ApiResponseStatus.Error) {
            val newState = state.value.copy(
                status = ApiResponseStatus.Error(SEND_MESSAGE_GENERIC_ERROR),
            )
            mutableStateFlow.emit(newState)
        }

    }

    private fun downloadCustomerConversation() {
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