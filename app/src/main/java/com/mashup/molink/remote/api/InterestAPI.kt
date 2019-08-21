package com.mashup.molink.remote.api

import com.mashup.molink.ui.interest.Model
import retrofit2.Call
import retrofit2.http.GET

interface InterestAPI {
    @GET("/api/v1/categories")
    fun getInterest(): Call<Model>

}