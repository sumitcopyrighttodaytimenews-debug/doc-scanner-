package com.example.ui.document

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.model.DocumentPage
import com.example.ui.viewmodels.DocScanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentDetailScreen(
    documentId: String,
    viewModel: DocScanViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    
    // In a real app we'd fetch this document uniquely, for now we will get it from the full list
    val documents by viewModel.documents.collectAsStateWithLifecycle()
    val document = documents.find { it.id == documentId }
    
    // We would also fetch pages from the viewmodel -> repository
    // Let's assume pages are passed or retrieved. Mock list for UI:
    val pages = listOf<DocumentPage>() // TODO: Load pages from DB

    if (document == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.center())
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(document.name) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO Share */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                    IconButton(onClick = { viewModel.deleteDocument(documentId); onNavigateBack() }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = { /* Run OCR */ }) {
                        Icon(Icons.Default.TextFields, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Extract Text")
                    }
                    Button(onClick = { /* Generate PDF */ }) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Create PDF")
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Tags: ${document.tags}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(16.dp))
            
            if (document.ocrText.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Recognized Text", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(document.ocrText, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text("Pages (${pages.size})", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(pages) { page ->
                    Card {
                        AsyncImage(
                            model = page.imageUri,
                            contentDescription = "Page ${page.pageIndex}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.7f),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}

fun Modifier.center() = this.fillMaxSize().wrapContentSize()
