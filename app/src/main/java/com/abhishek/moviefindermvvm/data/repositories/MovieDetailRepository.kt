package com.abhishek.moviefindermvvm.data.repositories

import com.abhishek.moviefindermvvm.data.model.MovieDetail
import com.abhishek.moviefindermvvm.data.network.ApiInterface
import com.abhishek.moviefindermvvm.data.network.SafeApiRequest

class MovieDetailRepository(
    private val api: ApiInterface
) : SafeApiRequest() {

    suspend fun getMovieDetail(
        title: String,
        apiKey: String
    ): MovieDetail {

        return apiRequest { api.getMovieDetailData(title, apiKey) }
    }


}