package com.dicoding.asclepius.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityHistoryAnalyzeBinding
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.utils.IMAGE_ARGUMENT
import com.dicoding.asclepius.utils.LABEL_RESULT
import com.dicoding.asclepius.utils.SCORE_RESULT

class ResultActivity : AppCompatActivityWithActionBack() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater).apply {
            setContentView(root)
            setupAppBar(appBarLayout.appBarAction, getString(R.string.result_page))
            setResultAnalyze()
        }
    }

    private fun ActivityResultBinding.setResultAnalyze() {
        resultImage.setImageURI(intent.getStringExtra(IMAGE_ARGUMENT)?.toUri())
        resultText.text =
            buildString {
                append("Category : ${intent.getStringExtra(LABEL_RESULT)} \n")
                append("Confidence Score : ${intent.getStringExtra(SCORE_RESULT)}")
            }
    }

}