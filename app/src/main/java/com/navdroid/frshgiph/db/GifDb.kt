package com.navdroid.frshgiph.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.navdroid.frshgiph.model.Data

@Database(
        entities = [Data::class],
        version = 1
)
abstract class GifDb : RoomDatabase() {
    abstract fun gifDao(): GifDataDao
    companion object {
        const val DB_NAME: String = "gif_db"
        private var INSTANCE: GifDb? = null

        fun getInstance(context: Context): GifDb? {
            if (INSTANCE == null) {
                synchronized(GifDb::class) {
                    INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            GifDb::class.java, DB_NAME
                    ).fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE
        }
    }


}