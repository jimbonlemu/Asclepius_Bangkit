package com.dicoding.asclepius.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = THEME_SETTING_PREFERENCE)

class ThemeSettingPreference private constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        private val THEME_KEY = booleanPreferencesKey(THEME_SETTING_KEY)

        @Volatile
        private var instance: ThemeSettingPreference? = null

        fun getInstanceOfThemeSettingPreference(dataStore: DataStore<Preferences>): ThemeSettingPreference =
            instance ?: synchronized(this) {
                instance ?: ThemeSettingPreference(dataStore)
            }.also { instance = it }
    }

    fun getSavedThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { savedSetting ->
            savedSetting[THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { setSavedSetting ->
            setSavedSetting[THEME_KEY] = isDarkModeActive
        }
    }
}