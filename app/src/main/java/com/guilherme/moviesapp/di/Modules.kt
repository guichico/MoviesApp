package com.guilherme.moviesapp.di

import com.guilherme.moviesapp.api.Api
import com.guilherme.moviesapp.api.MovieApi
import com.guilherme.moviesapp.viewmodel.MoviesListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val MoviesModule = module {
    single { Api.retrofit.create(MovieApi::class.java) }

    viewModel { MoviesListViewModel(get()) }
}

val moviesModule = listOf(MoviesModule)