package com.mashup.molink.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mashup.molink.data.model.Folder
import com.mashup.molink.data.model.Link

@Database(entities = [Folder::class, Link::class], version = 1)
abstract class MoLinkDataBase: RoomDatabase() {

    abstract fun getFolderDao(): FolderDao

    abstract fun getLinkDao(): LinkDao

    companion object {

        private var INSTANCE: MoLinkDataBase? = null

        fun getInstance(context: Context): MoLinkDataBase {

            if(INSTANCE == null) {
                synchronized(MoLinkDataBase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MoLinkDataBase::class.java,
                        "molink.db")
                        //테스트용 : 빌드 시 마다 기존 데이터베이스 삭제
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }

            return INSTANCE!!
        }

    }

}