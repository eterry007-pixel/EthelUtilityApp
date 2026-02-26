package com.example.ethelutilityapp.helper

import com.example.ethelutilityapp.model.CurrencyResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET("latest")
    suspend fun getLatestRates(@Query("from") from: String): CurrencyResponse
}

object CurrencyRetrofit {
    private const val BASE_URL = "https://api.frankfurter.app/"

    val api: CurrencyApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApi::class.java)
    }
}
