package com.leehendryp.stoneandroidchallenge.feed.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [RoomJoke::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class JokeDatabase : RoomDatabase() {

    abstract fun roomJokeDao(): RoomJokeDao

    companion object {
        private const val DATABASE = "joke_db"

        @Volatile
        private var INSTANCE: JokeDatabase? = null

        fun getDatabase(context: Context): JokeDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JokeDatabase::class.java,
                    DATABASE
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
