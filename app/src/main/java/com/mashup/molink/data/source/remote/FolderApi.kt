package com.mashup.molink.data.source.remote

import com.mashup.molink.data.model.Category
import com.mashup.molink.data.model.CommonData
import com.mashup.molink.data.model.Folder
import io.reactivex.Single
import retrofit2.http.*

interface FolderApi {

    @GET("/api/v1/folders/all")
    fun getFoldersAll(
    ): Single<CommonData<List<Folder>>>

    @POST("/api/v1/categories/folders")
    fun postCategoryFolders(
        @Body category: Category
    ): Single<CommonData<List<Folder>>>

    @POST("/api/v1/folders")
    fun postFolders(
        @Body folder: Folder
    ): Single<CommonData<Folder>>

    @PUT("/api/v1/folders/{id}")
    fun putFolders(
        @Path("id") id: Int,
        @Body folder: Folder
    ): Single<CommonData<Any>>

    @DELETE("/api/v1/folders/{id}")
    fun deleteFolders(
        @Path("id") id: Int
    ): Single<CommonData<Any>>
}