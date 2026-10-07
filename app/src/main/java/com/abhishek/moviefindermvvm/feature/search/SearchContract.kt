package com.abhishek.moviefindermvvm.feature.search

import com.abhishek.moviefindermvvm.domain.model.Movie

data class SearchUiState(
    val query: String = "Batman",
    val movies: List<Movie> = emptyList(),
    val favoriteIds: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val canLoadMore: Boolean = false,
    val error: String? = null,
)

sealed interface SearchIntent {
    data class QueryChanged(val query: String) : SearchIntent
    data object SubmitSearch : SearchIntent
    data object LoadNextPage : SearchIntent
    data class MovieSelected(val imdbId: String) : SearchIntent
    data class FavoriteToggled(val imdbId: String) : SearchIntent
    data object Retry : SearchIntent
}

sealed interface SearchEffect {
    data class OpenMovie(val imdbId: String) : SearchEffect
    data class Message(val value: String) : SearchEffect
}
