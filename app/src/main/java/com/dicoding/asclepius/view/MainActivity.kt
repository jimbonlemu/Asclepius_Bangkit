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
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.EntityAnalyzeHistory
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.utils.IMAGE_ARGUMENT
import com.dicoding.asclepius.utils.LABEL_RESULT
import com.dicoding.asclepius.utils.SCORE_RESULT
import com.dicoding.asclepius.view_model.AnalyzeHistoryViewModel
import com.dicoding.asclepius.view_model_factory.AnalyzeHistoryViewModelFactory
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private var currentImageUri: Uri? = null
    private lateinit var binding: ActivityMainBinding

    private val analyzeHistoryViewModel: AnalyzeHistoryViewModel by viewModels {
        AnalyzeHistoryViewModelFactory.getInstanceOfAnalyzeHistoryViewModelFactory(this@MainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            setupAppBar()
            buttonActionOnClick(analyzeHistoryViewModel)

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

    private fun ActivityMainBinding.buttonActionOnClick(
        analyzeHistoryViewModel: AnalyzeHistoryViewModel
    ) {
        galleryButton.setOnClickListener {
            startGallery()
        }
        analyzeButton.setOnClickListener {
            currentImageUri?.let { it1 -> analyzeImage(it1, analyzeHistoryViewModel) } ?: showToast(
                "Image Not Found"
            )
        }
    }

    private fun startGallery() {
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCropper(uri: Uri) {

        val fileName = "image_cropped_${Date().time}.jpg"
        val destination = Uri.fromFile(File(cacheDir, fileName))

        UCrop.of(uri, destination)
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
            previewImageView.setImageURI(uriValue)
        }
    }

    private fun ActivityMainBinding.analyzeImage(
        uriImage: Uri,
        analyzeHistoryViewModel: AnalyzeHistoryViewModel
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            isProgressBarEnabled(true)
            try {
                withContext(Dispatchers.IO) {
                    ImageClassifierHelper(
                        context = this@MainActivity,
                        mlResultHandler = object : ImageClassifierHelper.MlResultHandler {
                            override fun errorResult(result: String) {
                                showToast("Error: $result")
                            }

                            override fun successResult(
                                result: List<Classifications>?,
                                executionTime: Long
                            ) {
                                result?.let { listClassification ->
                                    if (listClassification.isNotEmpty() && listClassification[0].categories.isNotEmpty()) {
                                        val sortedCategories =
                                            listClassification[0].categories.sortedByDescending { it?.score }
                                        val entityHistory = EntityAnalyzeHistory(
                                            label = sortedCategories[0].label,
                                            confidenceScore = NumberFormat.getPercentInstance()
                                                .format(sortedCategories[0].score),
                                            image = uriImage.toString(),
                                            date = SimpleDateFormat(
                                                "dd/MM/yyyy HH:mm:ss",
                                                Locale.getDefault()
                                            ).format(Calendar.getInstance().time)
                                        )
                                        moveToResult(
                                            sortedCategories[0].label,
                                            NumberFormat.getPercentInstance()
                                                .format(sortedCategories[0].score)
                                        )
                                        analyzeHistoryViewModel.insertAnalyzeHistory(entityHistory)
                                    }
                                }
                            }
                        }
                    ).classifyStaticImage(uriImage)
                }
            } catch (e: Exception) {
                Log.d("MainActivity", e.message.toString())
            } finally {
                isProgressBarEnabled(false)
            }
        }
    }


    private fun moveToResult(
        label: String,
        confidenceScore: String
    ) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(IMAGE_ARGUMENT, currentImageUri.toString())
            putExtra(LABEL_RESULT, label)
            putExtra(SCORE_RESULT, confidenceScore)
        }
        startActivity(intent)
    }


    private fun ActivityMainBinding.isProgressBarEnabled(isLoading: Boolean) {
        progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        galleryButton.isEnabled = !isLoading
        analyzeButton.isEnabled = !isLoading
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
