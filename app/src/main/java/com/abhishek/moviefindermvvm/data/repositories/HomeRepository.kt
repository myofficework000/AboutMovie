package com.abhishek.moviefindermvvm.data.repositories

import com.abhishek.moviefindermvvm.data.model.SearchResults
import com.abhishek.moviefindermvvm.data.network.ApiInterface
import com.abhishek.moviefindermvvm.data.network.SafeApiRequest

class HomeRepository(
    private val api: ApiInterface
) : SafeApiRequest() {

    suspend fun getMovies(
        searchTitle: String,
        apiKey: String,
        pageIndex: Int
    ): SearchResults {

        return apiRequest { api.getSearchResultData(searchTitle, apiKey, pageIndex) }
    }


}