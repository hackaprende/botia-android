package app.botia.android.customers.repository

import app.botia.android.core.api.ApiResponseStatus
import app.botia.android.core.api.Network
import app.botia.android.customers.api.mappers.CustomerDTOMapper
import app.botia.android.customers.api.requests.ToggleBotEnabledRequest
import app.botia.android.customers.api.requests.TurnOffNeedCustomAttentionRequest
import app.botia.android.customers.api.services.CustomerApiService
import app.botia.android.customers.model.Customer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CustomerRepository {
    fun getCompanyCustomers(companyId: Int): Flow<ApiResponseStatus<List<Customer>>>

    fun toggleBotEnabledForCustomer(customerId: Int, newValue: Boolean):
            Flow<ApiResponseStatus<Unit>>

    fun turnOffNeedCustomerAttentionForCustomer(customerId: Int):
            Flow<ApiResponseStatus<Unit>>
}

class CustomerRepositoryImpl @Inject constructor(
    private val customerApiService: CustomerApiService,
    private val network: Network,
) : CustomerRepository {
    override fun getCompanyCustomers(companyId: Int) =
        network.makeNetworkCall {
            val customersResponse = customerApiService.getCompanyCustomers(companyId)

            val status = customersResponse.status
            if (status != "success") {
                throw Exception(status)
            }

            val customerDTOList = customersResponse.customers
            val customerDTOMapper = CustomerDTOMapper()
            customerDTOMapper.fromCustomerDTOListToCustomerDomainList(customerDTOList)
        }

    override fun toggleBotEnabledForCustomer(customerId: Int, newValue: Boolean) =
        network.makeNetworkCall {
            val toggleBotEnabledRequest = ToggleBotEnabledRequest(newValue)

            customerApiService.toggleBotEnabledForCustomer(
                customerId,
                toggleBotEnabledRequest
            )

            // If there is a problem, makeNetworkCall will catch it,
            // if not there is no need to return anything
            Unit
        }

    override fun turnOffNeedCustomerAttentionForCustomer(customerId: Int) =
        network.makeNetworkCall {
            val turnOffNeedCustomAttentionRequest = TurnOffNeedCustomAttentionRequest()

            customerApiService.turnOffNeedCustomerAttentionForCustomer(
                customerId,
                turnOffNeedCustomAttentionRequest,
            )

            // If there is a problem, makeNetworkCall will catch it,
            // if not there is no need to return anything
            Unit
        }
}