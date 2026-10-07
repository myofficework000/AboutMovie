package com.abhishek.moviefindermvvm.data.repository

import com.abhishek.moviefindermvvm.BuildConfig
import com.abhishek.moviefindermvvm.core.common.Resource
import com.abhishek.moviefindermvvm.data.remote.MovieDetailsResponse
import com.abhishek.moviefindermvvm.data.remote.MovieDto
import com.abhishek.moviefindermvvm.data.remote.OmdbApi
import com.abhishek.moviefindermvvm.domain.model.Movie
import com.abhishek.moviefindermvvm.domain.model.MovieDetails
import com.abhishek.moviefindermvvm.domain.model.MoviePage
import com.abhishek.moviefindermvvm.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OmdbMovieRepository @Inject constructor(
    private val api: OmdbApi,
) : MovieRepository {
    override fun searchMovies(query: String, page: Int): Flow<Resource<MoviePage>> = flow {
        emit(Resource.Loading)
        val response = api.searchMovies(query = query, page = page, apiKey = BuildConfig.OMDB_API_KEY)
        if (response.response == "True") {
            emit(Resource.Success(MoviePage(response.search.orEmpty().map(MovieDto::toMovie), response.totalResults?.toIntOrNull() ?: 0)))
        } else {
            emit(Resource.Error(response.error ?: "No movies found."))
        }
    }.catch { emit(Resource.Error(it.message ?: "Could not load movies. Check your connection.")) }

    override fun movieDetails(imdbId: String): Flow<Resource<MovieDetails>> = flow {
        emit(Resource.Loading)
        val response = api.movieDetails(imdbId = imdbId, apiKey = BuildConfig.OMDB_API_KEY)
        if (response.response == "True") emit(Resource.Success(response.toMovieDetails()))
        else emit(Resource.Error(response.error ?: "Movie details are unavailable."))
    }.catch { emit(Resource.Error(it.message ?: "Could not load movie details.")) }
}

private fun MovieDto.toMovie() = Movie(id, title, year, poster.takeUnless { it == "N/A" }.orEmpty(), type)

private fun MovieDetailsResponse.toMovieDetails() = MovieDetails(
    id, title, year, poster.takeUnless { it == "N/A" }.orEmpty(), type, rated, released, runtime,
    genre, director, writer, actors, plot, language, country, awards, imdbRating, imdbVotes,
)
