package com.abhishek.moviefindermvvm.di

import com.abhishek.moviefindermvvm.data.remote.OmdbApi
import com.abhishek.moviefindermvvm.data.repository.DataStoreFavoritesRepository
import com.abhishek.moviefindermvvm.data.repository.OmdbMovieRepository
import com.abhishek.moviefindermvvm.domain.repository.FavoritesRepository
import com.abhishek.moviefindermvvm.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds abstract fun bindMovieRepository(implementation: OmdbMovieRepository): MovieRepository
    @Binds abstract fun bindFavoritesRepository(implementation: DataStoreFavoritesRepository): FavoritesRepository
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides @Singleton
    fun provideApi(): OmdbApi {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        val client = OkHttpClient.Builder().addInterceptor(logger).build()
        return Retrofit.Builder().baseUrl("https://www.omdbapi.com/").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build().create(OmdbApi::class.java)
    }
}
