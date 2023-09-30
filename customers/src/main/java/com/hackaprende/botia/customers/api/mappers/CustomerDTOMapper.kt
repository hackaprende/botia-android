package com.hackaprende.botia.customers.api.mappers

import com.hackaprende.botia.customers.api.dto.CustomerDTO
import com.hackaprende.botia.customers.model.Customer

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