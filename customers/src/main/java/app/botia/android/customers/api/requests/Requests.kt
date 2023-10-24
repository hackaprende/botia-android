package app.botia.android.customers.api.requests

import com.squareup.moshi.Json

class ToggleBotEnabledRequest(@field:Json(name = "is_bot_enabled") val enabled: Boolean)

class TurnOffNeedCustomAttentionRequest(
    @field:Json(name = "need_custom_attention") val needCustomAttention: Boolean = false
)

class SendMessageToCustomerRequest(
    @field:Json(name = "company_id") val companyId: Int,
    @field:Json(name = "customer_id") val customerId: Int,
    @field:Json(name = "message_to_send") val messageToSend: String
)