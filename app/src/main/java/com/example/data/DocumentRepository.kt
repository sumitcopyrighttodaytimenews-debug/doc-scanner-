package com.example.data

import com.example.model.DocumentPage
import com.example.model.ScannedDocument
import kotlinx.coroutines.flow.Flow

class DocumentRepository(private val documentDao: DocumentDao) {
    val allDocuments: Flow<List<ScannedDocument>> = documentDao.getAllDocuments()

    fun getDocumentsByFolder(folder: String): Flow<List<ScannedDocument>> = documentDao.getDocumentsByFolder(folder)

    fun searchDocuments(query: String): Flow<List<ScannedDocument>> = documentDao.searchDocuments(query)

    fun getDocumentById(id: String): Flow<ScannedDocument> = documentDao.getDocumentById(id)

    suspend fun insertDocument(document: ScannedDocument) = documentDao.insertDocument(document)

    suspend fun deleteDocumentById(id: String) = documentDao.deleteDocumentById(id)

    suspend fun renameDocument(id: String, newName: String) = documentDao.renameDocument(id, newName)

    fun getPagesForDocument(documentId: String): Flow<List<DocumentPage>> = documentDao.getPagesForDocument(documentId)

    suspend fun insertPage(page: DocumentPage) = documentDao.insertPage(page)

    suspend fun deletePageById(pageId: String) = documentDao.deletePageById(pageId)
}
