package com.example.playlistmarket.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SearchHistoryPreferences(
    private val dataStore: DataStore<Preferences>
) {
    private val preferencesKey = stringPreferencesKey("search_history")
    private val MAX_ENTRIES = 10
    private val SEPARATOR = ","

    suspend fun addEntry(word: String) {
        if (word.isEmpty()) return

        dataStore.edit { preferences ->
            val historyString = preferences[preferencesKey].orEmpty()
            val history = if (historyString.isNotEmpty()) {
                historyString.split(SEPARATOR).toMutableList()
            } else {
                mutableListOf()
            }

            history.removeAll { it.equals(word, ignoreCase = true) } 
            history.add(0, word)

            while (history.size > MAX_ENTRIES) {
                history.removeAt(history.lastIndex)
            }

            preferences[preferencesKey] = history.joinToString(SEPARATOR)
        }
    }

    suspend fun getEntries(): List<String> {
        return dataStore.data.map { preferences ->
            val historyString = preferences[preferencesKey].orEmpty()
            if (historyString.isNotEmpty()) {
                historyString.split(SEPARATOR)
            } else {
                emptyList()
            }
        }.first()
    }
}