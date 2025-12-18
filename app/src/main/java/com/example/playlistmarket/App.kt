package com.example.playlistmarket

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.playlistmarket.data.db.AppDatabase
import com.example.playlistmarket.data.preferences.SearchHistoryPreferences

// Создаем расширение для получения DataStore (Singleton)
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class App : Application() {

    lateinit var database: AppDatabase
    lateinit var searchHistoryPreferences: SearchHistoryPreferences

    override fun onCreate() {
        super.onCreate()

        // Инициализация базы данных
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "database.db"
        )
            .allowMainThreadQueries() // В идеале убрать, но для простоты миграции пока оставим (лучше использовать корутины везде)
            .build()

        // Инициализация истории поиска
        searchHistoryPreferences = SearchHistoryPreferences(dataStore)
    }
}