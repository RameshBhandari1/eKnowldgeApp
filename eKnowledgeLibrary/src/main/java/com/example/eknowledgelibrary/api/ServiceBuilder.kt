package com.example.finalassignment.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceBuilder {
    private const val BASE_URL = "http://10.0.2.2:90/"
    //private const val BASE_URL = "http://192.168.137.206:90/"
    //private const val BASE_URL = "http://localhost:90/"
    //private const val BASE_URL = "http://192.168.1.71:90/"
    var token : String? = null
    var usertype : String? = null
    var username : String? = null
    var userID : String? = null
    private val okHttp = OkHttpClient.Builder()

    //create retrofit builder
    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp.build())

    //create retrofit instance
    private val retrofit = retrofitBuilder.build()

    // Load image path in Service Builder class
    fun loadImagePath(): String {
        return BASE_URL+ "uploads/"
    }

    //Generic function
    fun <T> buildService(serviceType : Class<T>): T{
        return retrofit.create(serviceType)
    }
}