package com.dicoding.asclepius.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.AdapterHistoryAnalyzed
import com.dicoding.asclepius.databinding.ActivityHistoryAnalyzeBinding
import com.dicoding.asclepius.view_model.AnalyzeHistoryViewModel
import com.dicoding.asclepius.view_model_factory.AnalyzeHistoryViewModelFactory

class HistoryAnalyzeActivity : AppCompatActivity() {

    private val analyzeHistoryViewModel: AnalyzeHistoryViewModel by viewModels {
        AnalyzeHistoryViewModelFactory.getInstanceOfAnalyzeHistoryViewModelFactory(this@HistoryAnalyzeActivity)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityHistoryAnalyzeBinding.inflate(layoutInflater).apply {
            setContentView(root)
            analyzeHistoryViewModel.getAllAnalyzeHistory().observe(this@HistoryAnalyzeActivity) {
                rvHistoryAnalyze.layoutManager = LinearLayoutManager(this@HistoryAnalyzeActivity)
                rvHistoryAnalyze.adapter = AdapterHistoryAnalyzed(it, this@HistoryAnalyzeActivity)
            }
        }
    }
}
