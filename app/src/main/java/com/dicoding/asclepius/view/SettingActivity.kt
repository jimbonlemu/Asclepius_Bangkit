package com.dicoding.asclepius.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivitySettingBinding
import com.dicoding.asclepius.utils.ThemeSettingPreference
import com.dicoding.asclepius.utils.datastore
import com.dicoding.asclepius.view_model.ThemeSettingViewModel
import com.dicoding.asclepius.view_model_factory.ThemeSettingViewModelFactory

class SettingActivity : AppCompatActivityWithActionBack() {
    private val themeSettingViewModel: ThemeSettingViewModel by viewModels {
        ThemeSettingViewModelFactory(
            ThemeSettingPreference.getInstanceOfThemeSettingPreference(
                application.datastore
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivitySettingBinding.inflate(layoutInflater).apply {
            setContentView(root)
            setupAppBar(appBarLayout.appBarAction, getString(R.string.setting_page))
            themeSetup()
        }
    }

    private fun ActivitySettingBinding.themeSetup() {
        themeSettingViewModel.apply {
            btnSwitchTheme.apply {
                getSavedThemeSetting()
                    .observe(this@SettingActivity) { isDarkMode ->
                        val setDarkMode =
                            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                        isChecked = isDarkMode
                        AppCompatDelegate.setDefaultNightMode(setDarkMode)
                    }
                setOnCheckedChangeListener { _, isChecked ->
                    saveThemeSetting(isChecked)
                }
            }
        }
    }
}