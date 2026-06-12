package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.home.HomeScreen
import com.example.ui.theme.DocScanTheme
import com.example.ui.viewmodels.DocScanViewModel
import com.example.ui.viewmodels.DocScanViewModelFactory

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      DocScanTheme {
        val app = application as DocScanApplication
        val viewModel: DocScanViewModel = viewModel(
          factory = DocScanViewModelFactory(app.repository)
        )
        
        DocScanApp(viewModel)
      }
    }
  }
}

@Composable
fun DocScanApp(viewModel: DocScanViewModel) {
  val navController = rememberNavController()

  Surface(
    modifier = Modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background
  ) {
    NavHost(navController = navController, startDestination = "home") {
      composable("home") {
        HomeScreen(
          viewModel = viewModel,
          onNavigateToCamera = { navController.navigate("camera") },
          onNavigateToDocument = { id -> navController.navigate("document/$id") }
        )
      }
      composable("camera") {
        com.example.ui.camera.CameraScreen(
          onNavigateBack = { navController.popBackStack() },
          onImageCaptured = { uri -> 
              // TODO: Navigate to crop/save screen or add page
              navController.popBackStack() 
          }
        )
      }
      composable("document/{id}") { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")
        if (id != null) {
          com.example.ui.document.DocumentDetailScreen(
            documentId = id,
            viewModel = viewModel,
            onNavigateBack = { navController.popBackStack() }
          )
        }
      }
    }
  }
}
