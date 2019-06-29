package com.mashup.molink.model

import android.os.Parcel
import android.os.Parcelable

data class Folder(
    val fid: Int,
    val title: String,
    val color: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(fid)
        parcel.writeString(title)
        parcel.writeString(color)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Folder> {
        override fun createFromParcel(parcel: Parcel): Folder {
            return Folder(parcel)
        }

        override fun newArray(size: Int): Array<Folder?> {
            return arrayOfNulls(size)
        }
    }
}