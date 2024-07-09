package com.example.app3.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.app3.database.model.ImageEntity
import com.example.app3.database.model.UrlConverter
import com.example.app3.utils.Constants


@Database(
    entities = [ImageEntity::class], version = 8, exportSchema = false
)
@TypeConverters(UrlConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao

    companion object {
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context, AppDatabase::class.java, Constants.DB_NAME
            ).fallbackToDestructiveMigration().build()
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) synchronized(this) {
                INSTANCE = buildDatabase(context)
            }
            return INSTANCE!!
        }
    }
}