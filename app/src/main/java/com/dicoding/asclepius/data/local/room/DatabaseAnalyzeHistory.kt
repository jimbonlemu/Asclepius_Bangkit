package com.dicoding.asclepius.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.data.local.entity.EntityAnalyzeHistory

@Database(entities = [EntityAnalyzeHistory::class], version = 1, exportSchema = false)
abstract class DatabaseAnalyzeHistory : RoomDatabase() {
    abstract fun daoAnalyzeHistory(): DaoAnalyzeHistory

    companion object {
        @Volatile
        private var instance: DatabaseAnalyzeHistory? = null
        fun getInstanceOfDatabaseAnalyzeHistory(context: Context): DatabaseAnalyzeHistory =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseAnalyzeHistory::class.java,
                    "AnalyzeHistory.db"
                ).build()
            }
    }
}