package com.dicoding.asclepius.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.utils.ThemeSettingPreference
import kotlinx.coroutines.launch

class ThemeSettingViewModel(private val themeSettingPreference: ThemeSettingPreference) : ViewModel() {

    fun getSavedThemeSetting(): LiveData<Boolean> {
        return themeSettingPreference.getSavedThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            themeSettingPreference.saveThemeSetting(isDarkModeActive)
        }
    }

}