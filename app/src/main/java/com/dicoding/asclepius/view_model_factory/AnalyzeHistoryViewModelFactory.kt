package com.dicoding.asclepius.view_model_factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.di.Injection
import com.dicoding.asclepius.repository.RepositoryAnalyzeHistory
import com.dicoding.asclepius.view_model.AnalyzeHistoryViewModel

class AnalyzeHistoryViewModelFactory(private val repositoryAnalyzeHistory: RepositoryAnalyzeHistory) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnalyzeHistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AnalyzeHistoryViewModel(repositoryAnalyzeHistory) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: AnalyzeHistoryViewModelFactory? = null

        fun getInstanceOfAnalyzeHistoryViewModelFactory(context: Context): AnalyzeHistoryViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: AnalyzeHistoryViewModelFactory(
                    Injection.getRepositoryAnalyzeHistory(context)
                )
            }.also { instance = it }
    }
}