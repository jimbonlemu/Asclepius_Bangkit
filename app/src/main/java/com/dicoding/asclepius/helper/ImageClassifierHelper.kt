package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import com.dicoding.asclepius.R
import com.dicoding.asclepius.utils.NUM_THREADS
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class ImageClassifierHelper(
    private var imgThreshold: Float = 0.1f,
    private var imgMaxResults: Int = 3,
    private val mlModelName: String = "cancer_classification.tflite",
    val context: Context,
    val mlResultHandler: MlResultHandler?
) {
    private var imageClassifier: ImageClassifier? = null

    interface MlResultHandler {
        fun errorResult(result: String)
        fun successResult(
            result: List<Classifications>?,
            executionTime: Long
        )
    }

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        try {

           val  imageOptions =ImageClassifier.ImageClassifierOptions.builder()
               .setScoreThreshold(imgThreshold)
               .setMaxResults(imgMaxResults).setBaseOptions(
                   BaseOptions.builder()
                       .setNumThreads(NUM_THREADS).build()
               ).build()
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                mlModelName,
                imageOptions
            )
        } catch (errorCaught: IllegalStateException) {
            mlResultHandler?.errorResult(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, "${errorCaught.message}")
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        if (imageClassifier == null) {
            setupImageClassifier()
        }
        val imageProcessor =
            ImageProcessor.Builder().add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                .add(CastOp(DataType.FLOAT32)).build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, imageUri))
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }.copy(Bitmap.Config.ARGB_8888, true)?.let { bitmap ->
            mlResultHandler?.successResult(
                imageClassifier?.classify(imageProcessor.process(TensorImage.fromBitmap(bitmap))),
                SystemClock.uptimeMillis()
            )
        }
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}
