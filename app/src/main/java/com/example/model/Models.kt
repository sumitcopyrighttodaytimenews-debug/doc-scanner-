package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "scanned_documents")
data class ScannedDocument(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val dateCreated: Long = System.currentTimeMillis(),
    val folder: String = "Personal", // Personal, Study, Office, Receipts
    val tags: String = "",
    val ocrText: String = "",
    val isPdfGenerated: Boolean = false,
    val pdfUri: String? = null,
    val pageCount: Int = 0
)

@Entity(tableName = "document_pages")
data class DocumentPage(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val documentId: String,
    val imageUri: String, // local file URI
    val pageIndex: Int
)
