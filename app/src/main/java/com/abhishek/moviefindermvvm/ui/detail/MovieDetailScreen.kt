package com.abhishek.moviefindermvvm.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.abhishek.moviefindermvvm.domain.model.MovieDetails
import com.abhishek.moviefindermvvm.feature.detail.DetailIntent
import com.abhishek.moviefindermvvm.feature.detail.DetailUiState
import com.abhishek.moviefindermvvm.feature.detail.DetailViewModel

@Composable
fun MovieDetailRoute(onBack: () -> Unit, viewModel: DetailViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MovieDetailScreen(state, viewModel::accept, onBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieDetailScreen(state: DetailUiState, onIntent: (DetailIntent) -> Unit, onBack: () -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text(state.movie?.title ?: "Movie details") }, navigationIcon = { IconButton(onBack) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Back") } }, actions = { IconButton(onClick = { onIntent(DetailIntent.FavoriteToggled) }) { Icon(if (state.isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder, "Toggle favorite") } }) }) { padding ->
        when {
            state.isLoading -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            state.movie != null -> Details(state.movie, Modifier.padding(padding))
            else -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Text(state.error ?: "Movie unavailable"); Spacer(Modifier.height(12.dp)); Button({ onIntent(DetailIntent.Retry) }) { Text("Try again") } } }
        }
    }
}

@Composable
private fun Details(movie: MovieDetails, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row { AsyncImage(movie.posterUrl, "${movie.title} poster", Modifier.size(width = 128.dp, height = 188.dp).clip(MaterialTheme.shapes.large), contentScale = ContentScale.Crop); Spacer(Modifier.width(16.dp)); Column(verticalArrangement = Arrangement.spacedBy(6.dp)) { Text(movie.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text(movie.year); Text("★ ${movie.imdbRating.ifBlank { "—" }}  ·  ${movie.runtime}"); Text(movie.genre) } }
        DetailSection("Overview", movie.plot)
        DetailSection("Cast", movie.actors)
        DetailSection("Crew", "Directed by ${movie.director}\nWritten by ${movie.writer}")
        DetailSection("Release", "${movie.released} · ${movie.rated} · ${movie.country}\n${movie.language}")
        if (movie.awards.isNotBlank() && movie.awards != "N/A") DetailSection("Awards", movie.awards)
    }
}

@Composable private fun DetailSection(title: String, value: String) { Column { Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold); Spacer(Modifier.height(4.dp)); Text(value.ifBlank { "Not available" }, style = MaterialTheme.typography.bodyMedium) } }
