package com.epsilon.botia.customers.api.requests

import com.squareup.moshi.Json

class ToggleBotEnabledRequest(@field:Json(name = "is_bot_enabled") val enabled: Boolean)

class TurnOffNeedCustomAttentionRequest(
    @field:Json(name = "need_custom_attention") val needCustomAttention: Boolean = false
)