package com.abhishek.moviefindermvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.abhishek.moviefindermvvm.ui.MovieFinderApp
import com.abhishek.moviefindermvvm.ui.theme.MovieFinderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieFinderTheme { MovieFinderApp() }
        }
    }
}
