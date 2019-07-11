package com.mashup.molink.data

import androidx.room.*
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface FolderDao {

    @Query("SELECT * FROM folders")
    fun getAllFolders(): Single<List<Folder>>

    @Query("SELECT * FROM folders WHERE parent_id = null")
    fun getMainFolders(): Single<List<Folder>>

    @Query("SELECT * FROM folders WHERE id = :id")
    fun getFolderById(id: Int): Single<Folder>

    @Query("SELECT * FROM folders WHERE parent_id = :parentId")
    fun getFoldersByParentId(parentId: Int): Single<List<Folder>>

    @Query("DELETE FROM folders WHERE id = :id")
    fun deleteFolderById(id: Int)

    @Query("DELETE FROM folders")
    fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg folder: Folder)

    @Update
    fun update(vararg folder: Folder)

    @Delete
    fun delete(vararg folder: Folder)
}