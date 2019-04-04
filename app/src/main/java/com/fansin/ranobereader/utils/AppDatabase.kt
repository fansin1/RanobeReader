package com.fansin.ranobereader.utils

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.fansin.ranobereader.data.Book

@Database(entities = [Book::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao

    companion object {
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase? {
            if (instance == null){
                synchronized(AppDatabase::class){
                    instance = Room.databaseBuilder(context.applicationContext,
                            AppDatabase::class.java, "database").build()
                }
            }
            return instance
        }

    }
}