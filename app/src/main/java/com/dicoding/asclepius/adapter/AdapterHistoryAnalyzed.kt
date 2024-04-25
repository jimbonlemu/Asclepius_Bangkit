package com.dicoding.asclepius.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.EntityAnalyzeHistory
import com.dicoding.asclepius.databinding.ItemHistoryAnalyzeBinding

class AdapterHistoryAnalyzed(
    private var listHistoryAnalyzed: List<EntityAnalyzeHistory>,
    private val context: Context
) : RecyclerView.Adapter<AdapterHistoryAnalyzed.ListViewHolder>() {
    class ListViewHolder(val binding: ItemHistoryAnalyzeBinding) :
        RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            ItemHistoryAnalyzeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = listHistoryAnalyzed.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        listHistoryAnalyzed.reversed()[position].apply {
            with(holder.binding) {
                Glide.with(root).load(image).into(ivHistoryAnalyzed)
                with(context.resources) {
                    tvLabelHistoryAnalyzed.text = getString(R.string.title_category, label)
                    tvScoreHistoryAnalyzed.text = getString(R.string.title_score, confidenceScore)
                    tvDateHistoryAnalyzed.text = getString(R.string.title_time, date)
                }
            }
        }
    }
}