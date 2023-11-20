package app.botia.android.core

const val BASE_URL = "https://mybotia.alwaysdata.net/"
const val AUTHENTICATION_ENDPOINT = "users/api-token-auth/"
const val USER_COMPANY_ENDPOINT = "users/user_company/"
const val COMPANY_CUSTOMERS_ENDPOINT = "messages/company_customers/{company_id}/"
const val UPDATE_CUSTOMER_ENDPOINT = "messages/customers/{customer_id}/"
const val CUSTOMER_MESSAGES_ENDPOINT = "messages/customer_conversation/{company_id}/{customer_id}/"
const val SEND_MESSAGE_TO_CUSTOMER_ENDPOINT = "messages/send_message_to_customer/"
const val SEND_START_CONVERSATION_TEMPLATE_TO_CUSTOMER_ENDPOINT = "messages/start_conversation_with_template/"

// Notification keys
const val NOTIFICATION_TYPE_KEY = "type"
const val NOTIFICATION_CUSTOMER_PHONE_KEY = "customer_phone"
const val NOTIFICATION_CUSTOMER_ID_KEY = "customer_id"
const val NOTIFICATION_COMPANY_ID_KEY = "company_id"
const val NOTIFICATION_TYPE_CUSTOMER_MESSAGE = "customer_message"
const val NOTIFICATION_TITLE = "notification_title"
const val NOTIFICATION_BODY = "notification_body"
