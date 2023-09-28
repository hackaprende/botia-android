package com.hackaprende.botia.core.api

import okhttp3.Interceptor
import okhttp3.Response

// For some reason I cannot call ApiServiceInterceptor from another module, I had to do this
// dirty hack
object ApiServiceInterceptorHandler {
    fun setSessionToken(sessionToken: String) {
        ApiServiceInterceptor.setSessionToken(sessionToken)
    }
}

object ApiServiceInterceptor : Interceptor {
    const val NEEDS_AUTH_HEADER_KEY = "needs_authentication"

    private var sessionToken: String? = null

    fun setSessionToken(token: String) {
        this.sessionToken = token
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        if (request.header(NEEDS_AUTH_HEADER_KEY) != null) {
            // needs credentials
            if (sessionToken == null) {
                throw RuntimeException("Need to be authenticated to perform this request")
            } else {
                val token = "Token ${sessionToken!!}"
                requestBuilder.addHeader("Authorization:  ", token)
            }
        }
        return chain.proceed(requestBuilder.build())
    }
}
