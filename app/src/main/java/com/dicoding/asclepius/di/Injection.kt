package com.dicoding.asclepius.di

import android.content.Context
import com.dicoding.asclepius.data.local.room.DatabaseAnalyzeHistory
import com.dicoding.asclepius.repository.RepositoryAnalyzeHistory

object Injection {
    fun getRepositoryAnalyzeHistory(context: Context): RepositoryAnalyzeHistory {
        return RepositoryAnalyzeHistory.getInstanceOfRepositoryAnalyzeHistory(
            DatabaseAnalyzeHistory.getInstanceOfDatabaseAnalyzeHistory(
                context
            ).daoAnalyzeHistory()
        )
    }
}