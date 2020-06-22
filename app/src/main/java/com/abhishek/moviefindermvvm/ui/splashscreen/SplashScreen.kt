package com.abhishek.moviefindermvvm.ui.splashscreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.abhishek.moviefindermvvm.R
import com.abhishek.moviefindermvvm.ui.home.HomeActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class SplashScreen : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Observable.interval(4, TimeUnit.SECONDS)
            .take(1)
            .observeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { aLong -> navigateToHome() }
    }

    private fun navigateToHome() {
        val homeIntent = Intent(this, HomeActivity::class.java)
        startActivity(homeIntent)
        finish()
    }
}

