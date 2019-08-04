package com.mashup.molink.data.repository

import com.mashup.molink.data.model.Link
import com.mashup.molink.data.source.local.LinkDao
import com.mashup.molink.data.source.remote.LinkApi
import com.mashup.molink.utils.Dlog
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LinkRepository(
    private val linkDao: LinkDao,
    private val linkApi: LinkApi
) {

    fun getAllLinks(): Single<List<Link>> {
        return linkDao.getAllLinks().subscribeOn(Schedulers.io())
    }

    fun getLinkById(id: Int): Single<Link> {
        return linkDao.getLinkById(id).subscribeOn(Schedulers.io())
    }

    fun flowableLinksByFolderId(folderId: Int): Flowable<List<Link>> {
        return linkDao.flowableLinksByFolderId(folderId).subscribeOn(Schedulers.io())
    }

    fun getLinksByFolderId(folderId: Int): Single<List<Link>> {
        return linkDao.getLinksByFolderId(folderId).subscribeOn(Schedulers.io())
    }

    fun insertLink(name: String, url: String, folderId: Int, onSuccess: () -> Unit): Disposable  {
        val link = Link(
            id = 0, name = name, url = url, folderId = folderId
        )

        return linkApi.postLinks(link)
            .flatMap {
                Single.fromCallable { linkDao.insert(it.data) }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Dlog.d(it.toString())
                onSuccess.invoke()
            }) {
                Dlog.e(it.message)
            }
    }

    fun updateLink(link: Link, onSuccess: () -> Unit): Disposable {

        return linkApi.putLinks(link.id, link)
            .flatMap {
                Single.fromCallable { linkDao.update(link) }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Dlog.d(it.toString())
                onSuccess.invoke()
            }) {
                Dlog.e(it.message)
            }
    }

    fun deleteLinkById(id: Int, onSuccess: () -> Unit): Disposable {

        return linkApi.deleteLinks(id)
            .flatMap {
                Single.fromCallable { linkDao.deleteLinkById(id) }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Dlog.d(it.toString())
                onSuccess.invoke()
            }) {
                Dlog.e(it.message)
            }
    }
}