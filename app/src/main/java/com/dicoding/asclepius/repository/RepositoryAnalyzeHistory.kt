package com.dicoding.asclepius.repository

import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.local.entity.EntityAnalyzeHistory
import com.dicoding.asclepius.data.local.room.DaoAnalyzeHistory

class RepositoryAnalyzeHistory private constructor(
    private val daoAnalyzeHistory: DaoAnalyzeHistory
) {
    fun getAllAnalyzeHistory(): LiveData<List<EntityAnalyzeHistory>> {
        return daoAnalyzeHistory.getAllAnalyzeHistory()
    }

    suspend fun insertAnalyzedHistory(entityAnalyzeHistory: EntityAnalyzeHistory) {
        return daoAnalyzeHistory.insertAnalyzeHistory(entityAnalyzeHistory)
    }

    companion object {
        @Volatile
        private var instance: RepositoryAnalyzeHistory? = null

        fun getInstanceOfRepositoryAnalyzeHistory(
            daoAnalyzeHistory: DaoAnalyzeHistory
        ): RepositoryAnalyzeHistory = instance ?: synchronized(this) {
            instance ?: RepositoryAnalyzeHistory(daoAnalyzeHistory).also { instance = it }
        }
    }
}