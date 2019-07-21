package com.mashup.molink.data.repository

import com.mashup.molink.data.model.Link
import com.mashup.molink.data.source.local.LinkDao
import com.mashup.molink.data.source.remote.LinkApi
import com.mashup.molink.extensions.runOnIoScheduler
import com.mashup.molink.utils.Dlog
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

    fun getLinksByFolderId(folderId: Int): Single<List<Link>> {
        return linkDao.getLinksByFolderId(folderId).subscribeOn(Schedulers.io())
    }

    fun deleteLinkById(id: Int): Disposable {

        return linkApi.deleteLinks(id)
            .subscribeOn(Schedulers.io())
            .subscribe({
                Dlog.d(it.toString())
                linkDao.deleteLinkById(id)
            }){
                Dlog.e(it.message)
            }
    }

    fun insertLink(name: String, url: String, folderId: Int, func: () -> Unit): Disposable {
        val link = Link(
            id = 0, name = name, url = url, folderId = folderId
        )

        return linkApi.postLinks(link)
            .flatMap {
                Dlog.d(it.toString())
                Single.just(linkDao.insert(it.data))
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { func }
            ) {
                Dlog.e(it.message)
            }

    }

    fun updateLink(link: Link): Disposable {

        return linkApi.putLinks(link.id, link)
            .subscribe({
                Dlog.d(it.toString())
                linkDao.update(link)
            }) {
                Dlog.e(it.message)

            }
    }
}