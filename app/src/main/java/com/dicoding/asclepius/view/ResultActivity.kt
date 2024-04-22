package com.dicoding.asclepius.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.AdapterListNews
import com.dicoding.asclepius.data.network.response.ArticlesItem
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.utils.IMAGE_ARGUMENT
import com.dicoding.asclepius.utils.LABEL_RESULT
import com.dicoding.asclepius.utils.SCORE_RESULT
import com.dicoding.asclepius.view_model.NewsViewModel

class ResultActivity : AppCompatActivityWithActionBack() {
    private val newsViewModel by viewModels<NewsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityResultBinding.inflate(layoutInflater).apply {
            setContentView(root)
            setupAppBar(appBarLayout.appBarAction, getString(R.string.result_page))
            setResultAnalyze()
            initRecyclerView()
            getAllNews()
        }
    }

    private fun ActivityResultBinding.setResultAnalyze() {
        resultImage.setImageURI(intent.getStringExtra(IMAGE_ARGUMENT)?.toUri())
        resultText.text = getString(R.string.title_category, intent.getStringExtra(LABEL_RESULT))
        tvConfidenceScore.text = getString(R.string.title_score, intent.getStringExtra(SCORE_RESULT))
    }

    private fun ActivityResultBinding.getAllNews() {
        with(newsViewModel) {
            listOfNews.observe(this@ResultActivity) {
                if (it != null) {
                    val filteredData = it.filter { articlesItem ->
                        articlesItem.author != null
                    }
                    setAdapterNewsData(filteredData)
                }
            }
            isLoadingNewsData.observe(this@ResultActivity) {
                isLoadingEnabled(it)
            }
        }
    }

    private fun ActivityResultBinding.initRecyclerView() {
        rvNews.apply {
            val layoutManager = LinearLayoutManager(this@ResultActivity)
            this.layoutManager = layoutManager
            val itemDecor = DividerItemDecoration(this@ResultActivity, layoutManager.orientation)
            addItemDecoration(itemDecor)
        }
    }

    private fun ActivityResultBinding.setAdapterNewsData(listNews: List<ArticlesItem>) {
        val adapter = AdapterListNews()
        adapter.submitList(listNews)
        rvNews.adapter = adapter
    }

    private fun ActivityResultBinding.isLoadingEnabled(isLoading: Boolean) {
        newsLoader.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}