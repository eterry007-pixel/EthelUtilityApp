package com.example.ethelutilityapp.helper

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

interface QuoteApi {
    @GET("api/1.0/?method=getQuote&lang=en&format=text")
    ////suspend fun: asynchronous code is called within a coroutine or another suspend function.
    suspend fun getQuote(): String
}

//creates a single, static instance (a singleton)
object RetrofitInstance {
    val api: QuoteApi by lazy {//defers the initialization of the variable until it is first accessed.
        Retrofit.Builder()
            .baseUrl("http://api.forismatic.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(QuoteApi::class.java)
    }
}