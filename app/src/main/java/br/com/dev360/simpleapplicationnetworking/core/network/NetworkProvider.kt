package br.com.dev360.simpleapplicationnetworking.core.network

import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class NetworkProvider(private val baseUrl: String) {
    val moshi: Moshi = Moshi.Builder()
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    fun <T> createService(serviceClass: Class<T>): T = retrofit.create(serviceClass)
}