package com.dicoding.asclepius.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.Date

class MainActivity : AppCompatActivity() {
    private var currentImageUri: Uri? = null
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            galleryButton.setOnClickListener { startGallery() }
            analyzeButton.setOnClickListener { }
        }

    }

    private fun startGallery() {
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
        if (value.resultCode == Activity.RESULT_OK){
            val resultValue = UCrop.getOutput(value.data!!)
            if (resultValue != null){
                currentImageUri = resultValue
                binding.showImage()
            }
        }else if(value.resultCode == UCrop.RESULT_ERROR){
            showToast("Error caused by : ${UCrop.getError(value.data!!)?.localizedMessage}")
        }
    }

    private fun startCropper(uri: Uri) {
        UCrop.of(uri, Uri.fromFile(File(cacheDir, "cropped_image_${Date().time}.jpg")))
            .withAspectRatio(1f, 1f).getIntent(this).apply {
                imageCropperLauncher.launch(this)
            }
    }

    private fun ActivityMainBinding.showImage() {
        currentImageUri?.let { uriValue ->
            Log.d("Image URI", "get Image $uriValue")
            previewImageView.setImageURI(uriValue)
        }
    }

    private fun analyzeImage() {
    }

    private fun moveToResult() {
        val intent = Intent(this, ResultActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}