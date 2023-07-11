package com.alqorut.mystory.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.alqorut.mystory.models.SesiLogin
import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SesiPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getSesi(): Flow<SesiLogin> {
        return dataStore.data.map { preferences ->
            SesiLogin(
                preferences[NAME_KEY] ?:"",
                preferences[EMAIL_KEY] ?:"",
                preferences[PASSWORD_KEY] ?:"",
                preferences[TOKEN_KEY] ?:"",
                preferences[STATE_KEY] ?: false
            )
        }
    }

    suspend fun saveSesi(sesi: SesiLogin) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = sesi.name
            preferences[EMAIL_KEY] = sesi.email
            preferences[PASSWORD_KEY] = sesi.password
            preferences[TOKEN_KEY] = sesi.token
            preferences[STATE_KEY] = sesi.isLogin
        }
    }

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = false
            preferences[TOKEN_KEY] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SesiPreference? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): SesiPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = SesiPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}