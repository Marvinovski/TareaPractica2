package com.amaurypm.videogamesrf.data.remote

import com.amaurypm.videogamesrf.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Me va a ayudar a obtener la instancia a retrofit
class RetrofitHelper {

    val apiKey = "8dbf5d2a27c4178b4b036c49ae7"

    val interceptor = HttpLoggingInterceptor().apply {
        level =
            HttpLoggingInterceptor.Level.BODY //respuesta al nivel del body en la operación de red
    }

    val client = OkHttpClient.Builder().apply {
        addInterceptor(interceptor)
        addInterceptor(ApiKeyInterceptor(apiKey))
    }.build()

    fun getRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}