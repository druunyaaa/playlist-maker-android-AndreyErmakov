package com.example.playlistmarket.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.playlistmarket.data.db.converters.DbConverter
import com.example.playlistmarket.data.db.dao.PlaylistDao
import com.example.playlistmarket.data.db.dao.TrackDao
import com.example.playlistmarket.data.db.entity.PlaylistEntity
import com.example.playlistmarket.data.db.entity.TrackEntity

@Database(entities = [PlaylistEntity::class, TrackEntity::class], version = 1, exportSchema = false)
@TypeConverters(DbConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    abstract fun trackDao(): TrackDao
}