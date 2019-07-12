package com.mashup.molink.interest

import retrofit2.Call
import retrofit2.http.GET

interface InterestAPI {
    @GET("/api/v1/categories")
    fun getInterest(): Call<Model>

}