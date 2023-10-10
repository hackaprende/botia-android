package app.botia.android.core

const val BASE_URL = "https://mybotia.alwaysdata.net/"
const val AUTHENTICATION_ENDPOINT = "users/api-token-auth/"
const val USER_COMPANY_ENDPOINT = "users/user_company/"
const val COMPANY_CUSTOMERS_ENDPOINT = "messages/company_customers/{company_id}/"
const val UPDATE_CUSTOMER_ENDPOINT = "messages/customers/{customer_id}/"

// Notification keys
const val NOTIFICATION_ACTION_KEY = "action"
const val NOTIFICATION_CUSTOMER_PHONE_KEY = "customer_phone"
const val NOTIFICATION_CUSTOMER_ID_KEY = "customer_id"
const val NOTIFICATION_ACTION_CUSTOMER_NEED_HELP = "customer_need_help"
