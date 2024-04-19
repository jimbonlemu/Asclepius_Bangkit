    package com.dicoding.asclepius.view_model_factory

    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.ViewModelProvider
    import com.dicoding.asclepius.utils.ThemeSettingPreference
    import com.dicoding.asclepius.view_model.ThemeSettingViewModel

    class ThemeSettingViewModelFactory(private val themeSettingPreference: ThemeSettingPreference) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ThemeSettingViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return ThemeSettingViewModel(themeSettingPreference) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class:" + modelClass.name)
        }
    }
