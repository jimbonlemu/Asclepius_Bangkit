package com.dicoding.asclepius.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.asclepius.data.local.entity.EntityAnalyzeHistory

@Dao
interface DaoAnalyzeHistory {
    @Query("SELECT * FROM EntityAnalyzeHistory")
    fun getAllAnalyzeHistory(): LiveData<List<EntityAnalyzeHistory>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAnalyzeHistory(entityAnalyzeHistory: EntityAnalyzeHistory)
}