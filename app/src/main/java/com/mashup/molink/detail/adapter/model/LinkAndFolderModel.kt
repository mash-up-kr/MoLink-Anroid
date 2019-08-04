package com.mashup.molink.detail.adapter.model

data class LinkAndFolderModel(
    //폴더 아이디
    val fid: Int = -1,

    //링크 아이디
    val lid: Int = -1,

    val title: String? = null,
    val color: String? = null,
    val url: String? = null,

    val viewType: Int = -1
)