package com.mashup.molink.data.source.local

import androidx.room.*
import com.mashup.molink.data.model.Folder
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface FolderDao {

    @Query("SELECT * FROM folders")
    fun flowableAllFolders(): Flowable<List<Folder>>

    @Query("SELECT * FROM folders")
    fun getAllFolders(): Single<List<Folder>>

    //@Query("SELECT * FROM folders WHERE parent_id = null")
    //fun getMainFolders(): Flowable<List<Folder>>

    @Query("SELECT * FROM folders WHERE id = :id")
    fun getFolderById(id: Int): Single<Folder>

    @Query("SELECT * FROM folders WHERE parent_id = :parentId")
    fun getFoldersByParentId(parentId: Int): Single<List<Folder>>

    @Query("DELETE FROM folders WHERE id = :id")
    fun deleteFolderById(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(folder: Folder)

    @Update
    fun update(folder: Folder)

    @Query("DELETE FROM folders")
    fun clearAll()
}