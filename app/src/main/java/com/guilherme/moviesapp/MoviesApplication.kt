package com.guilherme.moviesapp

import androidx.multidex.MultiDexApplication
import com.guilherme.moviesapp.di.moviesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MoviesApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)

            modules(moviesModule)
        }
    }
}