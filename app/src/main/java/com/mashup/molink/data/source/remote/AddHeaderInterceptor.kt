package com.mashup.molink.data.source.remote

import com.mashup.molink.BaseApplication
import okhttp3.Interceptor
import okhttp3.Response

class AddHeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .addHeader("phone_uuid", BaseApplication.androidId)
                .addHeader("Content-Type", "application/json")
                .build()
        )
    }
}