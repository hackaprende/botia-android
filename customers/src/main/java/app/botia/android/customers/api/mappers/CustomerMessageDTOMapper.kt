package app.botia.android.customers.api.mappers

import app.botia.android.customers.api.dto.CustomerMessageDTO
import app.botia.android.customers.model.CustomerMessage

class CustomerMessageDTOMapper {
    private fun fromCustomerMessageDTOToCustomerMessageDomain(customerMessageDTO: CustomerMessageDTO) =
        CustomerMessage(
            customerMessageDTO.customerAnswer,
            customerMessageDTO.userAnswer,
            customerMessageDTO.messageId,
            customerMessageDTO.timestamp,
            customerMessageDTO.customerPhone,
            customerMessageDTO.messagingProduct,
            customerMessageDTO.functionName,
            customerMessageDTO.functionCall,
        )

    fun fromCustomerDTOListToCustomerDomainList(customerMessageDTOList: List<CustomerMessageDTO>) =
        customerMessageDTOList.map {
            fromCustomerMessageDTOToCustomerMessageDomain(it)
        }
}