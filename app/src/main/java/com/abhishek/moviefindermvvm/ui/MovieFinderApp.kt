package com.abhishek.moviefindermvvm.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.abhishek.moviefindermvvm.ui.detail.MovieDetailRoute
import com.abhishek.moviefindermvvm.ui.search.MovieSearchRoute

private const val SearchRoute = "search"
private const val DetailRoute = "detail/{movieId}"

@Composable
fun MovieFinderApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = SearchRoute) {
        composable(SearchRoute) {
            MovieSearchRoute(onMovieClick = { id -> navController.navigate("detail/$id") })
        }
        composable(
            route = DetailRoute,
            arguments = listOf(navArgument("movieId") { type = NavType.StringType }),
        ) {
            MovieDetailRoute(onBack = navController::popBackStack)
        }
    }
}
