package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.model.DocumentPage
import com.example.model.ScannedDocument

@Database(entities = [ScannedDocument::class, DocumentPage::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun documentDao(): DocumentDao
}
