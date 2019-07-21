package com.mashup.molink.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "folders",
    foreignKeys = [
        ForeignKey(
            entity = Folder::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("parent_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Folder(
    @PrimaryKey
    val id: Int,
    val name: String,
    val color: String,

    @ColumnInfo(name = "parent_id")
    @field:SerializedName("parent_id")
    val parentId: Int?
)