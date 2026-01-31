package com.example.stitchcounterv3.feature.library

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.stitchcounterv3.domain.model.Project
import com.example.stitchcounterv3.domain.model.ProjectType
import com.example.stitchcounterv3.feature.destinations.ProjectDetailScreenDestination
import com.example.stitchcounterv3.feature.navigation.RootNavGraph
import com.example.stitchcounterv3.feature.navigation.RootNavigationViewModel
import com.example.stitchcounterv3.feature.navigation.SheetScreen
import com.example.stitchcounterv3.feature.sharedComposables.RowProgressIndicator
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlin.math.roundToInt


@RootNavGraph
@Destination
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = hiltViewModel(),
    rootNavigationViewModel: RootNavigationViewModel,
    navigator: DestinationsNavigator
) {
    val projects by viewModel.projects.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            if (uiState.isMultiSelectMode) {
                MultiSelectTopBar(
                    selectedCount = uiState.selectedProjectIds.size,
                    totalCount = projects.size,
                    onSelectAll = { viewModel.selectAllProjects() },
                    onClearSelection = { viewModel.clearSelection() },
                    onDelete = { viewModel.requestBulkDelete() },
                    onCancel = { viewModel.toggleMultiSelectMode() }
                )
            } else {
                LibraryTopBar(
                    onEnterMultiSelect = { viewModel.toggleMultiSelectMode() },
                    hasProjects = projects.isNotEmpty()
                )
            }
        }
    ) { paddingValues ->
        Surface(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading -> {
                    LoadingState(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    )
                }
                projects.isEmpty() -> {
                    EmptyLibraryState(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = paddingValues.calculateTopPadding(),
                            bottom = 80.dp,
                            start = 16.dp,
                            end = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(projects) { project ->
                            SwipeableProjectRow(
                                project = project,
                                isSelected = uiState.selectedProjectIds.contains(project.id),
                                isMultiSelectMode = uiState.isMultiSelectMode,
                                onOpen = { 
                                    if (!uiState.isMultiSelectMode) {
                                        when (project.type) {
                                            ProjectType.SINGLE -> {
                                                rootNavigationViewModel.showBottomSheet(SheetScreen.SingleCounter(project.id))
                                            }
                                            ProjectType.DOUBLE -> {
                                                rootNavigationViewModel.showBottomSheet(SheetScreen.DoubleCounter(project.id))
                                            }
                                        }
                                    }
                                },
                                onSelect = { viewModel.toggleProjectSelection(project.id) },
                                onDelete = { viewModel.requestDelete(project) },
                                onToggleMultiSelect = { viewModel.toggleMultiSelectMode() },
                                onInfoClick = {
                                    if (!uiState.isMultiSelectMode && project.id > 0) {
                                        navigator.navigate(ProjectDetailScreenDestination(projectId = project.id))
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (uiState.showDeleteConfirmation) {
        DeleteConfirmationDialog(
            projectCount = uiState.projectsToDelete.size,
            onConfirm = { viewModel.confirmDelete() },
            onDismiss = { viewModel.cancelDelete() }
        )
    }
}

@Composable
private fun SwipeableProjectRow(
    project: Project,
    isSelected: Boolean,
    isMultiSelectMode: Boolean,
    onOpen: () -> Unit,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
    onToggleMultiSelect: () -> Unit,
    onInfoClick: () -> Unit
) {
    val swipeThreshold = 80.dp
    val density = LocalDensity.current
    val swipeThresholdPx = with(density) { swipeThreshold.toPx() }
    
    var offsetX by remember { mutableFloatStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = tween(durationMillis = 300),
        label = "swipe_offset"
    )

    LaunchedEffect(isMultiSelectMode) {
        if (isMultiSelectMode) {
            offsetX = 0f
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (!isMultiSelectMode) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(swipeThreshold)
                    .align(Alignment.CenterEnd)
                    .background(
                        MaterialTheme.colorScheme.error,
                        RoundedCornerShape(16.dp)
                    )
                    .clickable {
                        if (offsetX < -swipeThresholdPx / 2) {
                            onDelete()
                            offsetX = 0f
                        }
                    }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onError,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        ProjectRow(
            project = project,
            isSelected = isSelected,
            isMultiSelectMode = isMultiSelectMode,
            swipeOffset = animatedOffsetX,
            onOpen = {
                if (offsetX == 0f) {
                    onOpen()
                } else {
                    offsetX = 0f
                }
            },
            onSelect = {
                offsetX = 0f
                onSelect()
            },
            onDelete = onDelete,
            onToggleMultiSelect = onToggleMultiSelect,
            onInfoClick = onInfoClick,
            onResetSwipe = { offsetX = 0f },
            modifier = Modifier
                .offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
                .pointerInput(project.id, isMultiSelectMode) {
                    if (!isMultiSelectMode) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                if (offsetX >= -swipeThresholdPx / 2) {
                                    offsetX = 0f
                                }
                            }
                        ) { _, dragAmount ->
                            val newOffset = (offsetX + dragAmount).coerceIn(-swipeThresholdPx, 0f)
                            offsetX = newOffset
                        }
                    }
                }
        )
    }
}

@Composable
private fun ProjectRow(
    modifier: Modifier = Modifier,
    project: Project,
    isSelected: Boolean,
    isMultiSelectMode: Boolean,
    swipeOffset: Float = 0f,
    onOpen: () -> Unit,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
    onToggleMultiSelect: () -> Unit,
    onInfoClick: () -> Unit,
    onResetSwipe: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(project.id, isMultiSelectMode) {
                if (!isMultiSelectMode) {
                    detectTapGestures(
                        onTap = {
                            if (swipeOffset < 0f) {
                                onResetSwipe()
                            } else {
                                onOpen()
                            }
                        },
                        onLongPress = {
                            onToggleMultiSelect()
                            onSelect()
                        }
                    )
                } else {
                    detectTapGestures(
                        onTap = {
                            onSelect()
                        }
                    )
                }
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected && isMultiSelectMode) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProjectImageOrCheckbox(
                project = project,
                isSelected = isSelected,
                isMultiSelectMode = isMultiSelectMode,
                onSelect = onSelect
            )
            
            ProjectInfoSection(
                project = project,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            
            if (!isMultiSelectMode) {
                ProjectActionButtons(
                    onInfoClick = onInfoClick,
                    onDelete = onDelete
                )
            }
        }
    }
}

@Composable
private fun ProjectImageOrCheckbox(
    project: Project,
    isSelected: Boolean,
    isMultiSelectMode: Boolean,
    onSelect: () -> Unit
) {
    val context = LocalContext.current
    
    if (isMultiSelectMode) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onSelect() },
            modifier = Modifier.size(24.dp)
        )
    } else {
        if (project.imagePath != null) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data(project.imagePath)
                        .build()
                ),
                contentDescription = "Project image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        } 
    }
}

@Composable
private fun ProjectInfoSection(
    project: Project,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ProjectTitle(project.title)
        ProjectStatsContent(project = project)
    }
}

@Composable
private fun ProjectTitle(
    title: String
) {
    Text(
        text = title.ifBlank { "Untitled Project" },
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun ProjectStatsContent(
    project: Project
) {
    when (project.type) {
        ProjectType.SINGLE -> {
            StatBadge(
                label = "Count",
                value = project.stitchCounterNumber.toString(),
                modifier = Modifier.fillMaxWidth()
            )
        }
        ProjectType.DOUBLE -> {
            DoubleProjectStats(
                project = project
            )
        }
    }
}

@Composable
private fun DoubleProjectStats(
    project: Project
) {
    val rowProgress: Float? = if (project.totalRows > 0) {
        (project.rowCounterNumber.toFloat() / project.totalRows.toFloat()).coerceIn(0f, 1f)
    } else {
        null
    }
    
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatBadge(
                label = "Stitches",
                value = project.stitchCounterNumber.toString(),
                modifier = Modifier.weight(1f)
            )
            StatBadge(
                label = "Rows",
                value = "${project.rowCounterNumber}${if (project.totalRows > 0) "/${project.totalRows}" else ""}",
                modifier = Modifier.weight(1f)
            )
        }
        
        if (rowProgress != null) {
            RowProgressIndicator(
                progress = rowProgress,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ProjectActionButtons(
    onInfoClick: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        IconButton(
            onClick = onInfoClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Project details",
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
        }
        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete project",
                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun StatBadge(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LibraryTopBar(
    onEnterMultiSelect: () -> Unit,
    hasProjects: Boolean = true
) {
    TopAppBar(
        title = {
            Text("Library")
        },
        actions = {
            if (hasProjects) {
                IconButton(onClick = onEnterMultiSelect) {
                    Icon(
                        imageVector = Icons.Default.SelectAll,
                        contentDescription = "Select multiple"
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MultiSelectTopBar(
    selectedCount: Int,
    totalCount: Int,
    onSelectAll: () -> Unit,
    onClearSelection: () -> Unit,
    onDelete: () -> Unit,
    onCancel: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = if (selectedCount > 0) {
                    "$selectedCount selected"
                } else {
                    "Select projects"
                }
            )
        },
        navigationIcon = {
            IconButton(onClick = onCancel) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel selection"
                )
            }
        },
        actions = {
            if (selectedCount == totalCount && totalCount > 0) {
                IconButton(onClick = onClearSelection) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Clear selection"
                    )
                }
            } else {
                IconButton(onClick = onSelectAll) {
                    Icon(
                        imageVector = Icons.Default.SelectAll,
                        contentDescription = "Select all"
                    )
                }
            }
            IconButton(
                onClick = onDelete,
                enabled = selectedCount > 0
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete selected",
                    tint = if (selectedCount > 0) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    }
                )
            }
        }
    )
}

@Composable
private fun LoadingState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyLibraryState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FolderOpen,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
            Text(
                text = "No Projects Yet",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Create your first project to start tracking your stitches",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    projectCount: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (projectCount == 1) {
                    "Delete Project?"
                } else {
                    "Delete Projects?"
                }
            )
        },
        text = {
            Text(
                text = if (projectCount == 1) {
                    "Are you sure you want to delete this project? This action cannot be undone."
                } else {
                    "Are you sure you want to delete $projectCount projects? This action cannot be undone."
                }
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

