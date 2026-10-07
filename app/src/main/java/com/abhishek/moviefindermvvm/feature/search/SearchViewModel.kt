package com.abhishek.moviefindermvvm.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.moviefindermvvm.core.common.Resource
import com.abhishek.moviefindermvvm.domain.repository.FavoritesRepository
import com.abhishek.moviefindermvvm.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(SearchUiState())
    val state = _state.asStateFlow()
    private val _effects = MutableSharedFlow<SearchEffect>()
    val effects = _effects.asSharedFlow()
    private var page = 1
    private var totalResults = 0

    init {
        viewModelScope.launch {
            favoritesRepository.observeFavoriteIds().collect { ids -> _state.update { it.copy(favoriteIds = ids) } }
        }
        search(reset = true)
    }

    fun accept(intent: SearchIntent) {
        when (intent) {
        is SearchIntent.QueryChanged -> _state.update { it.copy(query = intent.query, error = null) }
        SearchIntent.SubmitSearch -> search(reset = true)
        SearchIntent.LoadNextPage -> {
            if (_state.value.canLoadMore && !_state.value.isLoadingMore) search(reset = false)
        }
        is SearchIntent.MovieSelected -> viewModelScope.launch { _effects.emit(SearchEffect.OpenMovie(intent.imdbId)) }
        is SearchIntent.FavoriteToggled -> viewModelScope.launch { favoritesRepository.toggle(intent.imdbId) }
        SearchIntent.Retry -> search(reset = page == 1)
        }
    }

    private fun search(reset: Boolean) {
        val query = _state.value.query.trim()
        if (query.length < 2) {
            viewModelScope.launch { _effects.emit(SearchEffect.Message("Enter at least two characters.")) }
            return
        }
        if (reset) {
            page = 1
            totalResults = 0
            _state.update { it.copy(isLoading = true, isLoadingMore = false, movies = emptyList(), canLoadMore = false, error = null) }
        } else {
            page += 1
            _state.update { it.copy(isLoadingMore = true, error = null) }
        }
        viewModelScope.launch {
            movieRepository.searchMovies(query, page).collectLatest { result ->
                when (result) {
                    Resource.Loading -> Unit
                    is Resource.Success -> {
                        totalResults = result.data.totalResults
                        _state.update { old ->
                            val merged = (if (reset) emptyList() else old.movies) + result.data.movies
                            old.copy(movies = merged.distinctBy { it.id }, isLoading = false, isLoadingMore = false,
                                canLoadMore = merged.size < totalResults, error = null)
                        }
                    }
                    is Resource.Error -> _state.update { it.copy(isLoading = false, isLoadingMore = false, error = result.message) }
                }
            }
        }
    }
}
