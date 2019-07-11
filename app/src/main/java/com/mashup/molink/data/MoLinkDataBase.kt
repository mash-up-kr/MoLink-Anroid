package com.mashup.molink.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

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
                        //TODO 테스트용 - 빌드 시 마다 기존 데이터베이스 삭제
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }

            return INSTANCE!!
        }

    }

}