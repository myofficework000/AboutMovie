package com.abhishek.moviefindermvvm.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.abhishek.moviefindermvvm.domain.model.Movie
import com.abhishek.moviefindermvvm.feature.search.SearchEffect
import com.abhishek.moviefindermvvm.feature.search.SearchIntent
import com.abhishek.moviefindermvvm.feature.search.SearchUiState
import com.abhishek.moviefindermvvm.feature.search.SearchViewModel

@Composable
fun MovieSearchRoute(
    onMovieClick: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbars = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is SearchEffect.OpenMovie -> onMovieClick(effect.imdbId)
                is SearchEffect.Message -> snackbars.showSnackbar(effect.value)
            }
        }
    }
    MovieSearchScreen(state, viewModel::accept, snackbars)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieSearchScreen(
    state: SearchUiState,
    onIntent: (SearchIntent) -> Unit,
    snackbars: SnackbarHostState,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("About Movie", fontWeight = FontWeight.Bold) }) },
        snackbarHost = { SnackbarHost(snackbars) },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
            OutlinedTextField(
                value = state.query,
                onValueChange = { onIntent(SearchIntent.QueryChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Search movies") },
                leadingIcon = { Icon(Icons.Outlined.Search, null) },
                trailingIcon = { IconButton(onClick = { onIntent(SearchIntent.SubmitSearch) }) { Icon(Icons.Outlined.Search, "Search") } },
            )
            LazyRow(contentPadding = PaddingValues(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(listOf("Batman", "Dune", "Spider-Man", "Inception")) { suggestion ->
                    AssistChip(onClick = { onIntent(SearchIntent.QueryChanged(suggestion)); onIntent(SearchIntent.SubmitSearch) }, label = { Text(suggestion) })
                }
            }
            when {
                state.isLoading -> Loading(Modifier.weight(1f))
                state.error != null && state.movies.isEmpty() -> ErrorState(state.error, { onIntent(SearchIntent.Retry) }, Modifier.weight(1f))
                state.movies.isEmpty() -> EmptyState(Modifier.weight(1f))
                else -> MovieList(state, onIntent, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun MovieList(state: SearchUiState, onIntent: (SearchIntent) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(vertical = 8.dp)) {
        items(state.movies, key = { it.id }) { movie ->
            MovieCard(movie, movie.id in state.favoriteIds, onClick = { onIntent(SearchIntent.MovieSelected(movie.id)) }, onFavorite = { onIntent(SearchIntent.FavoriteToggled(movie.id)) })
        }
        item {
            when {
                state.isLoadingMore -> Loading(Modifier.fillMaxWidth().padding(20.dp))
                state.canLoadMore -> Button(onClick = { onIntent(SearchIntent.LoadNextPage) }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) { Text("Load more movies") }
                state.error != null -> Button(onClick = { onIntent(SearchIntent.Retry) }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) { Text("Try again") }
            }
        }
    }
}

@Composable
private fun MovieCard(movie: Movie, favorite: Boolean, onClick: () -> Unit, onFavorite: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(model = movie.posterUrl, contentDescription = "${movie.title} poster", modifier = Modifier.size(width = 72.dp, height = 106.dp).clip(MaterialTheme.shapes.medium), contentScale = ContentScale.Crop)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(movie.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(4.dp))
                Text("${movie.year} · ${movie.type.replaceFirstChar { it.uppercase() }}", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onFavorite) { Icon(if (favorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder, if (favorite) "Remove from favorites" else "Add to favorites") }
        }
    }
}

@Composable private fun Loading(modifier: Modifier = Modifier) = androidx.compose.foundation.layout.Box(modifier, contentAlignment = Alignment.Center) { CircularProgressIndicator() }
@Composable private fun EmptyState(modifier: Modifier = Modifier) = androidx.compose.foundation.layout.Box(modifier, contentAlignment = Alignment.Center) { Text("Search for a movie to begin.") }
@Composable private fun ErrorState(message: String, retry: () -> Unit, modifier: Modifier = Modifier) = androidx.compose.foundation.layout.Box(modifier, contentAlignment = Alignment.Center) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Text(message); Spacer(Modifier.height(12.dp)); Button(retry) { Text("Try again") } } }
