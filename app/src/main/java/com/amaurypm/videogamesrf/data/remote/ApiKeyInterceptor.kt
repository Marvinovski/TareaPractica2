package com.amaurypm.videogamesrf.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "APIKEY $apiKey")
            //.addHeader("X-Api-Key", apiKey)
            //.addHeader("Authorization", "Bearer $apiKey")
            .build()
        return chain.proceed(request)
    }
}