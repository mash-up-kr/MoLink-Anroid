package com.mashup.molink.data

import android.content.Context
import com.mashup.molink.data.repository.FolderRepository
import com.mashup.molink.data.repository.LinkRepository
import com.mashup.molink.data.source.local.MoLinkDataBase
import com.mashup.molink.data.source.remote.ApiProvider

object Injection {

    fun provideFolderRepository(context: Context): FolderRepository {
        val database = MoLinkDataBase.getInstance(context)
        return FolderRepository(
            database.getFolderDao(),
            ApiProvider.provideFolderApi()
        )
    }

    fun provideLinkRepository(context: Context): LinkRepository {
        val database = MoLinkDataBase.getInstance(context)
        return LinkRepository(
            database.getLinkDao(),
            ApiProvider.provideLinkApi()
        )
    }
}