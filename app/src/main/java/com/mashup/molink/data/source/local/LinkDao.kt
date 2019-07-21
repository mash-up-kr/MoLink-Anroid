package com.mashup.molink.data.source.local

import androidx.room.*
import com.mashup.molink.data.model.Link
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface LinkDao {

    @Query("SELECT * FROM links")
    fun getAllLinks(): Single<List<Link>>

    @Query("SELECT * FROM links WHERE id = :id")
    fun getLinkById(id: Int): Single<Link>

    @Query("SELECT * FROM links WHERE folder_id = :folderId")
    fun getLinksByFolderId(folderId: Int): Single<List<Link>>

    @Query("SELECT * FROM links WHERE folder_id = :folderId")
    fun flowableLinksByFolderId(folderId: Int): Flowable<List<Link>>

    @Query("DELETE FROM links WHERE id = :id")
    fun deleteLinkById(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(link: Link)

    @Update
    fun update(link: Link)

    @Query("DELETE FROM links")
    fun clearAll()
}