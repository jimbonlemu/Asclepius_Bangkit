package com.dicoding.asclepius.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.utils.FIRST_ANALYZE_LABEL_RESULT
import com.dicoding.asclepius.utils.FIRST_SCORE_RESULT
import com.dicoding.asclepius.utils.IMAGE_ARGUMENT
import com.dicoding.asclepius.utils.SECOND_ANALYZE_LABEL_RESULT
import com.dicoding.asclepius.utils.SECOND_SCORE_RESULT

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater).apply {
            setContentView(root)

            resultImage.setImageURI(intent.getStringExtra(IMAGE_ARGUMENT)?.toUri())
            firstLabel.text = "${intent.getStringExtra(FIRST_ANALYZE_LABEL_RESULT)} 1"
            firstScore.text = "${intent.getStringExtra(FIRST_SCORE_RESULT)} 1"
            secondLabel.text = "${intent.getStringExtra(SECOND_ANALYZE_LABEL_RESULT)} 2"
            secondScore.text = "${intent.getStringExtra(SECOND_SCORE_RESULT)} 2"
        }
    }

}