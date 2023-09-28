package com.hackaprende.botia.customers.model

class Customer(
    val id: Int,
    val name: String,
    val phoneNumber: String,
    val lastInteractionTimestamp: Int,
    val isBotEnabled: Boolean,
)

