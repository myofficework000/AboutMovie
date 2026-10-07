package com.abhishek.moviefindermvvm.domain.repository

import com.abhishek.moviefindermvvm.core.common.Resource
import com.abhishek.moviefindermvvm.domain.model.MovieDetails
import com.abhishek.moviefindermvvm.domain.model.MoviePage
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun searchMovies(query: String, page: Int): Flow<Resource<MoviePage>>
    fun movieDetails(imdbId: String): Flow<Resource<MovieDetails>>
}

interface FavoritesRepository {
    fun observeFavoriteIds(): Flow<Set<String>>
    suspend fun toggle(imdbId: String)
}
