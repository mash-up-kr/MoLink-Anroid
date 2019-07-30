package com.mashup.molink.data.model

import com.google.gson.annotations.SerializedName

data class Category(
    @field:SerializedName("category_name")
    val categoryName: List<String>
)