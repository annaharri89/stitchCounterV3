package com.example.stitchcounterv3.feature.projectDetail

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.activity.compose.BackHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.stitchcounterv3.feature.navigation.RootNavGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@Composable
fun ProjectDetailContent(
    uiState: ProjectDetailUiState,
    viewModel: ProjectDetailViewModel,
    context: Context,
    showDiscardDialog: Boolean,
    onDismissDiscardDialog: () -> Unit,
    onDiscard: () -> Unit,
    onCreateProject: (() -> Unit)?
) {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val savedImagePath = saveImageToInternalStorage(context, it, uiState.project?.id ?: 0)
            savedImagePath?.let { path ->
                viewModel.updateImagePath(path)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        val isNewProject = uiState.project?.id == null || uiState.project.id == 0

        ProjectDetailTopBar(isNewProject)

        OutlinedTextField(
            value = uiState.title,
            onValueChange = { viewModel.updateTitle(it) },
            label = { Text("Project Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text("Enter project title") },
            isError = uiState.titleError != null,
            supportingText = uiState.titleError?.let { { Text(it) } }
        )

        ProjectImageSelector(
            imagePath = uiState.imagePath,
            onImageClick = { imagePickerLauncher.launch("image/*") },
            onRemoveImage = { viewModel.updateImagePath(null) },
            modifier = Modifier.fillMaxWidth()
        )

        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        val projectNotCreated = onCreateProject != null && uiState.project?.id != null && uiState.project.id > 0

        if (projectNotCreated) {
            Button(
                onClick = onCreateProject,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Project")
            }
        }
    }

    if (showDiscardDialog) {
        val isTitleEmpty = uiState.title.isBlank()
        AlertDialog(
            onDismissRequest = onDismissDiscardDialog,
            title = {
                Text(if (isTitleEmpty) "Title Required" else "Discard Changes?")
            },
            text = {
                Text(
                    if (isTitleEmpty) {
                        "Project title is required. Do you want to discard this project?"
                    } else {
                        "You have unsaved changes. Are you sure you want to discard them?"
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDiscard()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Discard")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissDiscardDialog) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ProjectImageSelector(
    imagePath: String?,
    onImageClick: () -> Unit,
    onRemoveImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        if (imagePath != null) {
            ProjectImageDisplay(
                imagePath = imagePath,
                onImageClick = onImageClick,
                onRemoveImage = onRemoveImage
            )
        } else {
            ProjectImagePlaceholder(onImageClick = onImageClick)
        }
    }
}

@Composable
private fun ProjectImageDisplay(
    imagePath: String,
    onImageClick: () -> Unit,
    onRemoveImage: () -> Unit
) {
    val context = LocalContext.current
    
    Box {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(context)
                    .data(imagePath)
                    .build()
            ),
            contentDescription = "Project image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { onImageClick() },
            contentScale = ContentScale.Crop
        )
        ProjectImageDeleteButton(
            onRemoveImage = onRemoveImage,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        )
    }
}

@Composable
private fun ProjectImageDeleteButton(
    onRemoveImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onRemoveImage,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Remove image",
            tint = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun ProjectImagePlaceholder(
    onImageClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onImageClick() }
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = .2f)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.AddPhotoAlternate,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = "Add Project Image",
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Text(
            text = "(You can add it later)",
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center
        )
    }
}

private fun saveImageToInternalStorage(context: Context, uri: Uri, projectId: Int): String? {
    return try {
        val imagesDir = File(context.filesDir, "project_images")
        if (!imagesDir.exists()) {
            imagesDir.mkdirs()
        }
        
        val fileName = "project_${projectId}_${System.currentTimeMillis()}.jpg"
        val file = File(imagesDir, fileName)
        
        context.contentResolver.openInputStream(uri)?.use { inputStream: InputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        
        file.absolutePath
    } catch (e: Exception) {
        android.util.Log.e("ProjectDetailScreen", "Error saving image", e)
        null
    }
}

@RootNavGraph
@Destination
@Composable
fun ProjectDetailScreen(
    projectId: Int,
    viewModel: ProjectDetailViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showDiscardDialog by remember { mutableStateOf(false) }

    LaunchedEffect(projectId) {
        if (uiState.project == null || uiState.project?.id != projectId) {
            viewModel.loadProjectById(projectId)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.dismissalResult.collect { result ->
            when (result) {
                is com.example.stitchcounterv3.domain.model.DismissalResult.Allowed -> {
                    navigator.popBackStack()
                }
                is com.example.stitchcounterv3.domain.model.DismissalResult.Blocked -> {
                }
                is com.example.stitchcounterv3.domain.model.DismissalResult.ShowDiscardDialog -> {
                    showDiscardDialog = true
                }
            }
        }
    }

    BackHandler {
        viewModel.attemptDismissal()
    }

    ProjectDetailContent(
        uiState = uiState,
        viewModel = viewModel,
        context = context,
        showDiscardDialog = showDiscardDialog,
        onDismissDiscardDialog = { showDiscardDialog = false },
        onDiscard = {
            viewModel.discardChanges()
            showDiscardDialog = false
            navigator.popBackStack()
        },
        onCreateProject = null
    )
}

@Composable
private fun ProjectDetailTopBar(isNewProject: Boolean) {
    Row {
        Text(
            text = if (isNewProject) {
                "New Project"
            } else {
                "Project Details"
            },
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
