package com.dicoding.asclepius.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.network.response.ArticlesItem
import com.dicoding.asclepius.databinding.ItemNewsBinding

class AdapterListNews : ListAdapter<ArticlesItem, AdapterListNews.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(news: ArticlesItem) {
            with(binding) {
                with(news) {
                    Glide.with(root).load(urlToImage).into(ivNewsImage)
                    tvAuthorNews.text = root.context.getString(R.string.author, author)
                    tvTitleNews.text = title
                    tvDescriptionNews.text = description
                    root.setOnClickListener {
                        it.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}