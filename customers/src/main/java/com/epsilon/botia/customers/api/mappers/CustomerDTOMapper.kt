package com.epsilon.botia.customers.api.mappers

import com.epsilon.botia.customers.api.dto.CustomerDTO
import com.epsilon.botia.customers.model.Customer

class CustomerDTOMapper {
    fun fromCustomerDTOToCustomerDomain(customerDTO: CustomerDTO) =
        Customer(
            customerDTO.id,
            customerDTO.name,
            customerDTO.phoneNumber,
            customerDTO.lastInteractionTimestamp,
            customerDTO.isBotEnabled,
        )

    fun fromCustomerDTOListToCustomerDomainList(customerDTOList: List<CustomerDTO>) =
        customerDTOList.map {
            fromCustomerDTOToCustomerDomain(it)
        }
}