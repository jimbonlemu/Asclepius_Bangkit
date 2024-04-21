package com.dicoding.asclepius.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.dicoding.asclepius.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.AdapterHistoryAnalyzed
import com.dicoding.asclepius.databinding.ActivityHistoryAnalyzeBinding
import com.dicoding.asclepius.view_model.AnalyzeHistoryViewModel
import com.dicoding.asclepius.view_model_factory.AnalyzeHistoryViewModelFactory

class HistoryAnalyzeActivity : AppCompatActivityWithActionBack() {

    private val analyzeHistoryViewModel: AnalyzeHistoryViewModel by viewModels {
        AnalyzeHistoryViewModelFactory.getInstanceOfAnalyzeHistoryViewModelFactory(this@HistoryAnalyzeActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityHistoryAnalyzeBinding.inflate(layoutInflater).apply {
            setContentView(root)
            setupAppBar(appBarLayout.appBarAction, getString(R.string.history_page))
            loadHistoryData()
        }
    }

    private fun ActivityHistoryAnalyzeBinding.loadHistoryData() {
        loaderHistory.visibility = View.VISIBLE
        analyzeHistoryViewModel.getAllAnalyzeHistory().observe(this@HistoryAnalyzeActivity) {
            rvHistoryAnalyze.layoutManager = LinearLayoutManager(this@HistoryAnalyzeActivity)
            rvHistoryAnalyze.adapter = AdapterHistoryAnalyzed(it, this@HistoryAnalyzeActivity)
            loaderHistory.visibility = View.GONE
        }
    }
}
