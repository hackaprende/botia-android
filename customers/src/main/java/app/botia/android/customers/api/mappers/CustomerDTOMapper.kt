package app.botia.android.customers.api.mappers

import app.botia.android.customers.api.dto.CustomerDTO
import app.botia.android.customers.model.Customer

class CustomerDTOMapper {
    fun fromCustomerDTOToCustomerDomain(customerDTO: CustomerDTO) =
        Customer(
            customerDTO.id,
            customerDTO.name,
            customerDTO.phoneNumber,
            customerDTO.lastInteractionTimestamp,
            customerDTO.isBotEnabled,
            customerDTO.needCustomAttention,
        )

    fun fromCustomerDTOListToCustomerDomainList(customerDTOList: List<CustomerDTO>) =
        customerDTOList.map {
            fromCustomerDTOToCustomerDomain(it)
        }
}