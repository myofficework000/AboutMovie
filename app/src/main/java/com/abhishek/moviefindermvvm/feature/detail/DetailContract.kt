package com.abhishek.moviefindermvvm.feature.detail

import com.abhishek.moviefindermvvm.domain.model.MovieDetails

data class DetailUiState(
    val movie: MovieDetails? = null,
    val isLoading: Boolean = true,
    val isFavorite: Boolean = false,
    val error: String? = null,
)

sealed interface DetailIntent {
    data object Retry : DetailIntent
    data object FavoriteToggled : DetailIntent
}
