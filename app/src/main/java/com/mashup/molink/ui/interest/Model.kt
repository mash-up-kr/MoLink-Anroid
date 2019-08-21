package com.mashup.molink.ui.interest

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName




data class Model(

    @SerializedName("data")
    @Expose
    val data: List<Data>,

    @SerializedName("message")
    @Expose
    val message: String? = null,

    @SerializedName("statusCode")
    @Expose
    val statusCode: Int? = null

)