package com.abhishek.moviefindermvvm.feature.search

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class SearchUiStateTest {
    @Test
    fun `initial state uses a useful query and cannot paginate`() {
        val state = SearchUiState()

        assertEquals("Batman", state.query)
        assertFalse(state.canLoadMore)
        assertFalse(state.isLoading)
    }
}
