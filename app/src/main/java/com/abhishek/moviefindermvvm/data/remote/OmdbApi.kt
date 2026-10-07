package com.abhishek.moviefindermvvm.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {
    @GET(".")
    suspend fun searchMovies(
        @Query("s") query: String,
        @Query("type") type: String = "movie",
        @Query("page") page: Int,
        @Query("apikey") apiKey: String,
    ): SearchResponse

    @GET(".")
    suspend fun movieDetails(
        @Query("i") imdbId: String,
        @Query("plot") plot: String = "full",
        @Query("apikey") apiKey: String,
    ): MovieDetailsResponse
}

data class SearchResponse(
    @SerializedName("Response") val response: String = "False",
    @SerializedName("Error") val error: String? = null,
    @SerializedName("totalResults") val totalResults: String? = null,
    @SerializedName("Search") val search: List<MovieDto>? = null,
)

data class MovieDto(
    @SerializedName("imdbID") val id: String,
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("Poster") val poster: String,
    @SerializedName("Type") val type: String,
)

data class MovieDetailsResponse(
    @SerializedName("Response") val response: String = "False",
    @SerializedName("Error") val error: String? = null,
    @SerializedName("imdbID") val id: String = "",
    @SerializedName("Title") val title: String = "",
    @SerializedName("Year") val year: String = "",
    @SerializedName("Poster") val poster: String = "",
    @SerializedName("Type") val type: String = "",
    @SerializedName("Rated") val rated: String = "",
    @SerializedName("Released") val released: String = "",
    @SerializedName("Runtime") val runtime: String = "",
    @SerializedName("Genre") val genre: String = "",
    @SerializedName("Director") val director: String = "",
    @SerializedName("Writer") val writer: String = "",
    @SerializedName("Actors") val actors: String = "",
    @SerializedName("Plot") val plot: String = "",
    @SerializedName("Language") val language: String = "",
    @SerializedName("Country") val country: String = "",
    @SerializedName("Awards") val awards: String = "",
    @SerializedName("imdbRating") val imdbRating: String = "",
    @SerializedName("imdbVotes") val imdbVotes: String = "",
)
