package com.abhishek.moviefindermvvm.domain.model

data class Movie(
    val id: String,
    val title: String,
    val year: String,
    val posterUrl: String,
    val type: String,
)

data class MoviePage(
    val movies: List<Movie>,
    val totalResults: Int,
)

data class MovieDetails(
    val id: String,
    val title: String,
    val year: String,
    val posterUrl: String,
    val type: String,
    val rated: String,
    val released: String,
    val runtime: String,
    val genre: String,
    val director: String,
    val writer: String,
    val actors: String,
    val plot: String,
    val language: String,
    val country: String,
    val awards: String,
    val imdbRating: String,
    val imdbVotes: String,
)
