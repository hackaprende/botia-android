package com.epsilon.botia.customers.api.dto

import com.squareup.moshi.Json

class CustomerDTO(
    val id: Int,
    val name: String,
    @field:Json(name = "phone_number") val phoneNumber: String,
    @field:Json(name = "last_interaction_timestamp") val lastInteractionTimestamp: Int,
    @field:Json(name = "is_bot_enabled") val isBotEnabled: Boolean,
)
