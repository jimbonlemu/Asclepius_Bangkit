package com.dicoding.asclepius.view_model

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.helper.ImageClassifierHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.vision.classifier.Classifications

class ImageClassifierViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorResult = MutableLiveData<String>()
    val errorResult: LiveData<String> = _errorResult

    private val _successResult = MutableLiveData<List<Category>?>()
    val successResult: LiveData<List<Category>?> = _successResult
    suspend fun analyzeImage(uri: Uri, context: Context) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                withContext(Dispatchers.IO) {
                    val classifyImage = ImageClassifierHelper(
                        context = context,
                        mlResultHandler = object : ImageClassifierHelper.MlResultHandler {
                            override fun errorResult(result: String) {
                                _errorResult.postValue(result)
                                _isLoading.postValue(false)
                            }

                            override fun successResult(
                                result: List<Classifications>?,
                                executionTime: Long
                            ) {
                                result?.let { resultValue ->
                                    if (resultValue.isNotEmpty() && resultValue[0].categories.isNotEmpty()) {
                                        _successResult.postValue(resultValue[0].categories.sortedByDescending { it.score })
                                    }
                                }
                            }
                        })
                    classifyImage.classifyStaticImage(uri)
                    withContext(Dispatchers.Main) {
                        _isLoading.postValue(false)
                    }
                }
            } catch (e: Exception) {
                Log.d("MainViewModel", e.message.toString())
                _isLoading.postValue(false)
            }
        }
    }
}
