package com.abhishek.moviefindermvvm.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.abhishek.moviefindermvvm.domain.repository.FavoritesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.favoriteDataStore by preferencesDataStore(name = "favorites")
private val FavoriteIds = stringSetPreferencesKey("movie_ids")

class DataStoreFavoritesRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) : FavoritesRepository {
    override fun observeFavoriteIds(): Flow<Set<String>> = context.favoriteDataStore.data.map { it[FavoriteIds].orEmpty() }

    override suspend fun toggle(imdbId: String) {
        context.favoriteDataStore.edit { preferences ->
            val current = preferences[FavoriteIds].orEmpty()
            preferences[FavoriteIds] = if (imdbId in current) current - imdbId else current + imdbId
        }
    }
}
