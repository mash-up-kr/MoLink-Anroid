package com.mashup.molink.data

import androidx.room.*
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface LinkDao {

    @Query("SELECT * FROM links")
    fun getAllLinks(): Single<List<Link>>

    @Query("SELECT * FROM links WHERE id = :id")
    fun getLinkById(id: Int): Single<Link>

    @Query("SELECT * FROM links WHERE folder_id = :folderId")
    fun getLinksByFolderId(folderId: Int): Single<List<Link>>

    @Query("DELETE FROM links WHERE id = :id")
    fun deleteLinkById(id: Int)


    @Query("DELETE FROM links")
    fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg folder: Link)

    @Update
    fun update(vararg folder: Link)

    @Delete
    fun delete(vararg folder: Link)
}