package com.mashup.molink.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "links",
    foreignKeys = [
        ForeignKey(
            entity = Folder::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("folder_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Link(
    @PrimaryKey
    val id: Int,
    val name: String,
    val url: String,

    @ColumnInfo(name = "folder_id")
    @field:SerializedName("folderId")
    val folderId: Int?
)