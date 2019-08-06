package com.guilherme.moviesapp

import androidx.multidex.MultiDexApplication
import com.guilherme.moviesapp.di.apiModule
import com.guilherme.moviesapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MoviesApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)

            modules(apiModule)
            modules(viewModelModule)
        }
    }
}