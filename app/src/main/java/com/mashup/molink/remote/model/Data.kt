package com.mashup.molink.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName




data class Data(

    @SerializedName("id")
    @Expose
    val id: Int? = null,

    @SerializedName("imgUrl")
    @Expose
    val imgUrl: String? = null,

    @SerializedName("name")
    @Expose
    val name: String? = null,

    var check: Boolean = false
)