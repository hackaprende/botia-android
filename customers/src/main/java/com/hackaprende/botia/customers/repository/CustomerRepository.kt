package com.hackaprende.botia.customers.repository

import com.hackaprende.botia.core.api.ApiResponseStatus
import com.hackaprende.botia.core.api.Network
import com.hackaprende.botia.customers.api.mappers.CustomerDTOMapper
import com.hackaprende.botia.customers.api.services.CustomerApiService
import com.hackaprende.botia.customers.model.Customer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CustomerRepository {
    fun getCompanyCustomers(companyId: Int): Flow<ApiResponseStatus<List<Customer>>>
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
}