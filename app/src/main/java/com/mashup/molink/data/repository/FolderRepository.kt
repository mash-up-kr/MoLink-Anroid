package com.mashup.molink.data.repository

import com.mashup.molink.data.StatusCode
import com.mashup.molink.data.model.Category
import com.mashup.molink.data.model.CommonData
import com.mashup.molink.data.model.Folder
import com.mashup.molink.data.source.local.FolderDao
import com.mashup.molink.data.source.remote.FolderApi
import com.mashup.molink.extensions.runOnIoScheduler
import com.mashup.molink.utils.Dlog
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FolderRepository(
    private val folderDao: FolderDao,
    private val folderApi: FolderApi
) {

    fun getAllFolders(): Single<List<Folder>> {
        return folderDao.getAllFolders()
            .subscribeOn(Schedulers.io())
    }

    private fun getFoldersAll() {
        folderApi.getFoldersAll()
            .subscribeOn(Schedulers.io())
            .subscribe({
                Dlog.d("getFoldersAll : $it")
                if(it.data.isNotEmpty()) {
                    it.data.forEach { folder ->
                        if(folder.color.isNullOrEmpty()) {
                            //TODO API 수정 필요
                            val tempFolder = folder.copy(color = "#990099")
                            folderDao.insert(tempFolder)
                        } else {
                            folderDao.insert(folder)
                        }
                    }
                }
            }){
                Dlog.e(it.message)
            }
    }

    fun flowableCategoryFolders(): Flowable<List<Folder>> {
        return folderDao.flowableAllFolders()
            .flatMap { folders ->
                val items = folders.filter { it.parentId == null }
                Flowable.just(items)
            }
            .subscribeOn(Schedulers.io())
    }

    fun getCategoryFolders(): Single<List<Folder>> {
        return folderDao.getAllFolders()
            .flatMap { folders ->
                val items = folders.filter { it.parentId == null }
                Single.just(items)
            }
            .subscribeOn(Schedulers.io())

    }

    fun getFolderById(id: Int): Single<Folder> {
        return folderDao.getFolderById(id)
            .subscribeOn(Schedulers.io())
    }

    fun getFoldersByParentId(parentId: Int): Single<List<Folder>> {
        return folderDao.getFoldersByParentId(parentId)
            .subscribeOn(Schedulers.io())
    }

    fun deleteFolderById(id: Int): Disposable {
        return folderApi.deleteFolders(id)
            .subscribeOn(Schedulers.io())
            .subscribe({
                Dlog.d(it.toString())
                folderDao.deleteFolderById(id)
            }){
                Dlog.e(it.message)
            }
    }

    fun insertFolder(title: String, color: String): Disposable {
        //TODO parentId = null 이면 500 error -> 0 이어야 합니다.
        val body = Folder(id = 0, name = title, color = color, parentId = 0)

        return folderApi.postFolders(body)
            .subscribeOn(Schedulers.io())
            .subscribe({
                Dlog.d(it.toString())
                val folder = it.data
                folderDao.insert(folder)
            }){
                Dlog.e(it.message)
            }

        /*return folderApi.getFolderId()
            .subscribeOn(Schedulers.io())
            .subscribe({

                Dlog.d("insertFolder -> folder id : $it")
                val folder =
                    Folder(id = it, name = title, color = color, parentId = null)
                folderDao.insert(folder)

            }) {
                Dlog.e(it.message)
            }*/
    }

    fun insertFolder(title: String, color: String, parentId: Int): Disposable {

        val body = Folder(id = 0, name = title, color = color, parentId = parentId)

        return folderApi.postFolders(body)
            .subscribeOn(Schedulers.io())
            .subscribe({
                Dlog.d(it.toString())
                val folder = it.data
                folderDao.insert(folder)
            }){
                Dlog.e(it.message)
            }

        /*return folderApi.getFolderId()
            .subscribeOn(Schedulers.io())
            .subscribe({

                Dlog.d("insertFolder -> folder id : $it")
                val folder =
                    Folder(id = it, name = title, color = color, parentId = parentId)
                folderDao.insert(folder)

            }) {
                Dlog.e(it.message)
            }*/
    }

    fun insertCategoryFolders(interests: List<String>): Disposable {

        val body = Category(interests)

        return folderApi.postCategoryFolders(body)
            .subscribeOn(Schedulers.io())
            .subscribe({
                Dlog.d(it.toString())
                if(it.statusCode == StatusCode.CREATED) {
                    it.data.forEach {
                        folderDao.insert(it)
                    }
                } else if(it.statusCode == StatusCode.ZEOR) {
                    getFoldersAll()
                }
            }){
                Dlog.e(it.message)
            }
    }

    fun updateFolder(folder: Folder): Disposable {
        return folderApi.putFolders(folder.id, folder)
            .subscribeOn(Schedulers.io())
            .subscribe({
                Dlog.d(it.toString())
                folderDao.update(folder)
            }){
                Dlog.e(it.message)
            }
    }
}