package com.abhishek.moviefindermvvm.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.moviefindermvvm.core.common.Resource
import com.abhishek.moviefindermvvm.domain.repository.FavoritesRepository
import com.abhishek.moviefindermvvm.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val favoritesRepository: FavoritesRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val imdbId = checkNotNull(savedStateHandle.get<String>("movieId"))
    private val _state = MutableStateFlow(DetailUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            favoritesRepository.observeFavoriteIds().collect { ids -> _state.update { it.copy(isFavorite = imdbId in ids) } }
        }
        load()
    }

    fun accept(intent: DetailIntent) {
        when (intent) {
            DetailIntent.Retry -> load()
            DetailIntent.FavoriteToggled -> viewModelScope.launch { favoritesRepository.toggle(imdbId) }
        }
    }

    private fun load() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null) }
        movieRepository.movieDetails(imdbId).collectLatest { result ->
            when (result) {
                Resource.Loading -> Unit
                is Resource.Success -> _state.update { it.copy(movie = result.data, isLoading = false, error = null) }
                is Resource.Error -> _state.update { it.copy(isLoading = false, error = result.message) }
            }
        }
    }
}
