package com.dicoding.asclepius.view

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityEntryBinding
import com.dicoding.asclepius.utils.SPLASH_SCREEN_DURATION
import com.dicoding.asclepius.utils.ThemeSettingPreference
import com.dicoding.asclepius.utils.datastore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class EntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityEntryBinding.inflate(layoutInflater).apply {
            setContentView(root)
            initDarkMode()
            ivSplash.startAnimation(
                AnimationUtils.loadAnimation(
                    this@EntryActivity, R.anim.anim_fade
                )
            )
            lifecycleScope.launch {
                delay(SPLASH_SCREEN_DURATION.seconds)
                startActivity(Intent(this@EntryActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun initDarkMode() {
        ThemeSettingPreference.getInstanceOfThemeSettingPreference(application.datastore)
            .getSavedThemeSetting().asLiveData()
            .observe(this) { darkModeActive ->
                val setMode =
                    if (darkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                AppCompatDelegate.setDefaultNightMode(setMode)
            }
    }

}
