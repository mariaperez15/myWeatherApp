package com.example.myweatherapp

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {

    companion object {
        private val Context.dataStore by preferencesDataStore("settings")
        val UPDATE_FREQUENCY_KEY = stringPreferencesKey("update_frequency")
    }

    val updateFrequency: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[UPDATE_FREQUENCY_KEY]
        }

    suspend fun setUpdateFrequency(frequency: String) {
        context.dataStore.edit { preferences ->
            preferences[UPDATE_FREQUENCY_KEY] = frequency
        }
    }
}
