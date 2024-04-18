package com.dicoding.asclepius.view_model_factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.view_model.MainViewModel
import org.tensorflow.lite.support.label.Category

class MainViewModelFactory(
    private var isLoading: Unit,
    private var errorResult: Unit,
    private var successResult: List<Category>
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(

            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class:" + modelClass.name)
    }
}