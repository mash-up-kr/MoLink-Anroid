package com.mashup.molink.data.model

data class CommonData<T>(
    val data: T,
    val message: String,
    val statusCode: Int
)