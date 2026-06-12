package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.model.DocumentPage
import com.example.model.ScannedDocument
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {
    @Query("SELECT * FROM scanned_documents ORDER BY dateCreated DESC")
    fun getAllDocuments(): Flow<List<ScannedDocument>>

    @Query("SELECT * FROM scanned_documents WHERE folder = :folder ORDER BY dateCreated DESC")
    fun getDocumentsByFolder(folder: String): Flow<List<ScannedDocument>>

    @Query("SELECT * FROM scanned_documents WHERE name LIKE '%' || :query || '%' OR ocrText LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%' ORDER BY dateCreated DESC")
    fun searchDocuments(query: String): Flow<List<ScannedDocument>>

    @Query("SELECT * FROM scanned_documents WHERE id = :id")
    fun getDocumentById(id: String): Flow<ScannedDocument>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: ScannedDocument)

    @Query("DELETE FROM scanned_documents WHERE id = :id")
    suspend fun deleteDocumentById(id: String)

    @Query("UPDATE scanned_documents SET name = :newName WHERE id = :id")
    suspend fun renameDocument(id: String, newName: String)

    // Pages
    @Query("SELECT * FROM document_pages WHERE documentId = :documentId ORDER BY pageIndex ASC")
    fun getPagesForDocument(documentId: String): Flow<List<DocumentPage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPage(page: DocumentPage)

    @Query("DELETE FROM document_pages WHERE id = :pageId")
    suspend fun deletePageById(pageId: String)
}
