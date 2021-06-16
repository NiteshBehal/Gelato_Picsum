package com.gelato.picsum.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gelato.picsum.data.models.ImageData
import com.gelato.picsum.data.models.RemoteKey

@Database(
    entities = [ImageData::class, RemoteKey::class],
    version = 1,
    exportSchema = false
)
abstract class ImagesDb: RoomDatabase() {
    abstract fun imageDao(): ImageDao
    abstract fun remoteDao(): RemoteDao

    companion object {
        @Volatile
        private var instance: ImagesDb? = null
        private val lock = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ImagesDb::class.java,
                "ImagesDb"
            ).build()
    }
}