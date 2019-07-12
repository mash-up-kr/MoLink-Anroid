package com.mashup.molink.data

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
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val url: String,

    @ColumnInfo(name = "folder_id")
    @field:SerializedName("folder_id")
    val folderId: Int?
)