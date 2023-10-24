package app.botia.android.customers.api.mappers

import app.botia.android.customers.api.dto.SendMessageErrorDTO
import app.botia.android.customers.model.SendMessageError

class SendMessageErrorMapper {
    fun fromSendMessageErrorDTOToDomain(
        sendMessageErrorDTO: SendMessageErrorDTO?,
    ): SendMessageError? {
        if (sendMessageErrorDTO == null) return null

        return SendMessageError(
            message = sendMessageErrorDTO.message,
            type = sendMessageErrorDTO.type,
            code = sendMessageErrorDTO.code,
            errorSubcode = sendMessageErrorDTO.errorSubcode,
            facebookTraceId = sendMessageErrorDTO.facebookTraceId,
        )
    }
}
