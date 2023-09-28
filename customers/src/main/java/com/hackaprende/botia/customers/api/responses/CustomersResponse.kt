package com.hackaprende.botia.customers.api.responses

import com.hackaprende.botia.customers.api.dto.CustomerDTO

class CustomersResponse(val status: String, val customers: List<CustomerDTO>)