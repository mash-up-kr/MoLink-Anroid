package com.mashup.molink.detail.adapter.model

data class LinkAndFolderModel(
    val fid: Int = -1,
    val lid: Int = -1,

    val title: String? = null,
    val color: String? = null,
    val url: String? = null,

    val viewType: Int = -1
)