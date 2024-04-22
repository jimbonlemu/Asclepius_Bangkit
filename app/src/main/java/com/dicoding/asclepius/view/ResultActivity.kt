package com.dicoding.asclepius.view

import android.os.Bundle
import androidx.core.net.toUri
import com.dicoding.asclepius.R
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
        resultText.text = getString(R.string.title_category, intent.getStringExtra(LABEL_RESULT))
        tvConfidenceScore.text = getString(R.string.title_score, intent.getStringExtra(SCORE_RESULT))
    }


}