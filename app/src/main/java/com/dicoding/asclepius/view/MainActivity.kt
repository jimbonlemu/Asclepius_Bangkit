package com.dicoding.asclepius.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.EntityAnalyzeHistory
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.utils.FIRST_ANALYZE_LABEL_RESULT
import com.dicoding.asclepius.utils.FIRST_SCORE_RESULT
import com.dicoding.asclepius.utils.IMAGE_ARGUMENT
import com.dicoding.asclepius.utils.SECOND_ANALYZE_LABEL_RESULT
import com.dicoding.asclepius.utils.SECOND_SCORE_RESULT
import com.dicoding.asclepius.view_model.AnalyzeHistoryViewModel
import com.dicoding.asclepius.view_model.ImageClassifierViewModel
import com.dicoding.asclepius.view_model_factory.AnalyzeHistoryViewModelFactory
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.label.Category
import java.io.File
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private var currentImageUri: Uri? = null
    private lateinit var binding: ActivityMainBinding
    private val imageClassifierViewModel: ImageClassifierViewModel by viewModels<ImageClassifierViewModel>()
    private val analyzeHistoryViewModel: AnalyzeHistoryViewModel by viewModels {
        AnalyzeHistoryViewModelFactory.getInstanceOfAnalyzeHistoryViewModelFactory(this@MainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            setupAppBar()
            galleryButton.setOnClickListener { startGallery() }
            analyzeButton.setOnClickListener {
                currentImageUri?.let {
                    lifecycleScope.launch {
                        analyzeImage(it, analyzeHistoryViewModel)
                    }
                } ?: showToast("Image not found")
            }
        }
    }

    private fun ActivityMainBinding.setupAppBar() {
        appBarAction.setOnMenuItemClickListener { itemClicked ->
            when (itemClicked.itemId) {
                R.id.menuToHistoryAnalyzed -> {
                    startActivity(Intent(this@MainActivity, HistoryAnalyzeActivity::class.java))
                    true
                }

                R.id.menuToSetting -> {
                    startActivity(Intent(this@MainActivity, SettingActivity::class.java))
                    true
                }

                else -> false
            }
        }
    }

    private fun startGallery() {
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCropper(uri: Uri) {
        UCrop.of(uri, Uri.fromFile(File(cacheDir, "cropped_image_${Date().time}.jpg")))
            .withAspectRatio(1f, 1f).getIntent(this).apply {
                imageCropperLauncher.launch(this)
            }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            startCropper(uri)
        } else {
            Log.d("Image Picker", "No Image was Picked")
        }
    }

    private val imageCropperLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { value ->
        if (value.resultCode == Activity.RESULT_OK) {
            val resultValue = UCrop.getOutput(value.data!!)
            if (resultValue != null) {
                currentImageUri = resultValue
                binding.showImage()
            }
        } else if (value.resultCode == UCrop.RESULT_ERROR) {
            showToast("Error caused by : ${UCrop.getError(value.data!!)?.localizedMessage}")
        }
    }


    private fun ActivityMainBinding.showImage() {
        currentImageUri?.let { uriValue ->
            Log.d("Image URI", "get Image $uriValue")
            previewImageView.setImageURI(uriValue)
        }
    }

    private suspend fun ActivityMainBinding.analyzeImage(
        uri: Uri,
        analyzeHistoryViewModel: AnalyzeHistoryViewModel
    ) {

        with(imageClassifierViewModel) {
            analyzeImage(uri, this@MainActivity)

            isLoading.observe(this@MainActivity) {
                isProgressBarEnabled(it)
            }

            errorResult.observe(this@MainActivity) {
                showToast(it)
            }

            successResult.observe(this@MainActivity) {
                it?.let {
                    analyzeHistoryViewModel.insertAnalyzeHistory(
                        EntityAnalyzeHistory(
                            label = it[0].label.toString(),
                            confidenceScore = formatNumberToPercent(it[0].score),
                            image = uri.toString(),
                            date = SimpleDateFormat(
                                "dd/MM/yyyy HH:mm:ss",
                                Locale.getDefault()
                            ).format(Calendar.getInstance().time)
                        )
                    )
                }
                moveToResult(it)
            }
        }
    }

    private fun ActivityMainBinding.isProgressBarEnabled(isLoading: Boolean) {
        progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        galleryButton.isEnabled = !isLoading
        analyzeButton.isEnabled = !isLoading
    }

    private fun moveToResult(resultAnalyzed: List<Category>?) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.apply {
            resultAnalyzed?.let {
                putExtra(IMAGE_ARGUMENT, currentImageUri.toString())
                putExtra(FIRST_ANALYZE_LABEL_RESULT, it[0].label)
                putExtra(FIRST_SCORE_RESULT, formatNumberToPercent(it[0].score))
                putExtra(SECOND_ANALYZE_LABEL_RESULT, it[1].label)
                putExtra(SECOND_SCORE_RESULT, formatNumberToPercent(it[1].score))
            }
        }
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun formatNumberToPercent(score: Float): String =
        NumberFormat.getPercentInstance().format(score)

}
