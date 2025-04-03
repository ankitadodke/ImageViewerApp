package com.example.imageviewerapp.ui.journeys.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.imageviewerapp.domain.model.UnsplashImage

@Composable
fun HomeScreen(
    navigateToDetail: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val gridState = rememberLazyGridState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search images...") },
            trailingIcon = {
                IconButton(onClick = { viewModel.onSearchExecute() }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
            },
            singleLine = true
        )

        when (uiState) {
            is HomeUiState.Loading -> {
                if ((uiState as? HomeUiState.Success)?.images.isNullOrEmpty()) {
                    LoadingIndicator()
                }
            }
            is HomeUiState.Success -> {
                val images = (uiState as HomeUiState.Success).images

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    state = gridState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(images) { image ->
                        if (image is UnsplashImage)
                        ImageGridItem(image = image, onImageClick = navigateToDetail)
                    }

                    item {
                        // Load more images when reaching the end
                        LaunchedEffect(gridState.canScrollForward) {
                            if (!gridState.canScrollForward) {
                                viewModel.loadNextPage()
                            }
                        }
                    }
                }
            }
            is HomeUiState.Error -> {
                ErrorMessage(message = (uiState as HomeUiState.Error).message) {
                    viewModel.loadImages()
                }
            }
        }
    }
}

@Composable
fun ImageGridItem(
    image: UnsplashImage,
    onImageClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onImageClick(image.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            AsyncImage(
                model = image.urls.regular?:"",
                contentDescription = image.description,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
            )

            // Photographer name overlay
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
            ) {
                Text(
                    text = "by ${image.user?.name}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorMessage(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error: $message",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}