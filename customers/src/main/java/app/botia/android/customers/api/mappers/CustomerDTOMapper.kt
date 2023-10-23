package app.botia.android.customers.api.mappers

import app.botia.android.customers.api.dto.CustomerDTO
import app.botia.android.customers.api.dto.CustomerMessageDTO
import app.botia.android.customers.model.Customer
import app.botia.android.customers.model.CustomerMessage

class CustomerDTOMapper {
    fun fromCustomerDTOToCustomerDomain(
        customerDTO: CustomerDTO,
        customerMessagesDTO: List<CustomerMessageDTO> = listOf()
    ): Customer {
        val customerMessageDTOMapper = CustomerMessageDTOMapper()
        val customerMessages =
            customerMessageDTOMapper.fromCustomerDTOListToCustomerDomainList(customerMessagesDTO)
        return Customer(
            customerDTO.id,
            customerDTO.name,
            customerDTO.phoneNumber,
            customerDTO.lastInteractionTimestamp,
            customerDTO.isBotEnabled,
            customerDTO.needCustomAttention,
            customerMessages,
        )
    }


    fun fromCustomerDTOListToCustomerDomainList(customerDTOList: List<CustomerDTO>) =
        customerDTOList.map {
            fromCustomerDTOToCustomerDomain(it)
        }
}