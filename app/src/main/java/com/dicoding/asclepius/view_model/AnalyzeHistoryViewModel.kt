package com.dicoding.asclepius.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.entity.EntityAnalyzeHistory
import com.dicoding.asclepius.repository.RepositoryAnalyzeHistory
import kotlinx.coroutines.launch

class AnalyzeHistoryViewModel(private val repositoryAnalyzeHistory: RepositoryAnalyzeHistory):ViewModel() {

    fun getAllAnalyzeHistory() = repositoryAnalyzeHistory.getAllAnalyzeHistory()

    fun insertAnalyzeHistory(entityAnalyzeHistory: EntityAnalyzeHistory){
        viewModelScope.launch {
            repositoryAnalyzeHistory.insertAnalyzedHistory(entityAnalyzeHistory)
        }
    }
}