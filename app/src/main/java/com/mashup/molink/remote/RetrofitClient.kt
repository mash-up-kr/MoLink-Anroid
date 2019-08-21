package com.mashup.molink.remote

import com.mashup.molink.remote.api.InterestAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val baseUrl = "http://ec2-52-79-252-3.ap-northeast-2.compute.amazonaws.com:8080"

    fun getInterestAPI(): InterestAPI = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(InterestAPI::class.java)
}