package com.mashup.molink.model

data class ItemLink(
    val fid: Int,
    val lid: Int,
    val title: String,
    val color: String,
    val url: String
) {
    var viewType = -1
}