package com.example.playlistmarket

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.playlistmarket.data.db.AppDatabase
import com.example.playlistmarket.data.preferences.SearchHistoryPreferences

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class App : Application() {

    lateinit var database: AppDatabase
    lateinit var searchHistoryPreferences: SearchHistoryPreferences

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "database.db"
        )
            .allowMainThreadQueries()
            .build()

        searchHistoryPreferences = SearchHistoryPreferences(dataStore)
    }
}