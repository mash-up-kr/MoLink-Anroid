package com.mashup.molink.data.source.remote

import com.mashup.molink.data.model.CommonData
import com.mashup.molink.data.model.Link
import io.reactivex.Single
import retrofit2.http.*

interface LinkApi {

    @POST("/api/v1/links")
    fun postLinks(
        @Body link: Link
    ): Single<CommonData<Link>>

    @PUT("/api/v1/links/{id}")
    fun putLinks(
        @Path("id") id: Int,
        @Body link: Link
    ): Single<CommonData<Any>>

    @DELETE("/api/v1/links/{id}")
    fun deleteLinks(
        @Path("id") id: Int
    ): Single<CommonData<Any>>

}