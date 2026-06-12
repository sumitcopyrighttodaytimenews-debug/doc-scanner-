package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.DocumentRepository
import com.example.model.ScannedDocument
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DocScanViewModel(private val repository: DocumentRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _currentFolder = MutableStateFlow("All")
    val currentFolder: StateFlow<String> = _currentFolder

    val documents: StateFlow<List<ScannedDocument>> = combine(
        repository.allDocuments,
        _searchQuery,
        _currentFolder
    ) { docs, query, folder ->
        var filtered = docs
        if (folder != "All") {
            filtered = filtered.filter { it.folder == folder }
        }
        if (query.isNotBlank()) {
            filtered = filtered.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.ocrText.contains(query, ignoreCase = true) ||
                it.tags.contains(query, ignoreCase = true)
            }
        }
        filtered
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFolderSelected(folder: String) {
        _currentFolder.value = folder
    }

    fun deleteDocument(id: String) {
        viewModelScope.launch {
            repository.deleteDocumentById(id)
        }
    }
}

class DocScanViewModelFactory(private val repository: DocumentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DocScanViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DocScanViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
