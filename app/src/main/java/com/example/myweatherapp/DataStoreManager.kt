package com.example.myweatherapp

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {

    companion object {
        private val Context.dataStore by preferencesDataStore(name = "settings")
        private val SELECTED_CITY_KEY = stringPreferencesKey("selected_city")
        private val SELECTED_CITY_LAT_KEY = doublePreferencesKey("selected_city_lat")
        private val SELECTED_CITY_LON_KEY = doublePreferencesKey("selected_city_lon")
        private val UPDATE_FREQUENCY_KEY = stringPreferencesKey("update_frequency")
    }

    val selectedCity: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_CITY_KEY]
        }

    val selectedCityLatitude: Flow<Double?> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_CITY_LAT_KEY]
        }

    val selectedCityLongitude: Flow<Double?> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_CITY_LON_KEY]
        }

    val updateFrequency: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[UPDATE_FREQUENCY_KEY]
        }

    suspend fun setSelectedCity(city: String) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_CITY_KEY] = city
        }
    }

    suspend fun setSelectedCityCoordinates(latitude: Double, longitude: Double) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_CITY_LAT_KEY] = latitude
            preferences[SELECTED_CITY_LON_KEY] = longitude
        }
    }

    suspend fun setUpdateFrequency(frequency: String) {
        context.dataStore.edit { preferences ->
            preferences[UPDATE_FREQUENCY_KEY] = frequency
        }
    }
}
