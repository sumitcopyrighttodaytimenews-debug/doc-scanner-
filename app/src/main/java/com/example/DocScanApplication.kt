package com.example

import android.app.Application
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.DocumentRepository
import com.example.model.ScannedDocument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DocScanApplication : Application() {
    
    lateinit var database: AppDatabase
        private set
        
    lateinit var repository: DocumentRepository
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "docscan_database"
        ).build()
        
        repository = DocumentRepository(database.documentDao())

        // Insert mock data if empty
        CoroutineScope(Dispatchers.IO).launch {
            val docs = repository.allDocuments.first()
            if (docs.isEmpty()) {
                repository.insertDocument(
                    ScannedDocument(
                        name = "Physics Assignment 1",
                        folder = "Study",
                        pageCount = 3,
                        tags = "physics, study",
                        ocrText = "Newton's laws of motion are three basic laws of classical mechanics..."
                    )
                )
                repository.insertDocument(
                    ScannedDocument(
                        name = "Coffee Receipt",
                        folder = "Receipts",
                        pageCount = 1,
                        tags = "expenses",
                        ocrText = "Total: $4.50"
                    )
                )
            }
        }
    }
}
