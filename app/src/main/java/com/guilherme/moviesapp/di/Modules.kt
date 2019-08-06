package com.guilherme.moviesapp.di

import com.guilherme.moviesapp.api.Api
import com.guilherme.moviesapp.api.MovieApi
import com.guilherme.moviesapp.viewmodel.MovieViewModel
import com.guilherme.moviesapp.viewmodel.MoviesListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ApiModule = module {
    single { Api.retrofit.create(MovieApi::class.java) }
}

val ViewModelsModule = module {
    viewModel { MoviesListViewModel(get()) }
    viewModel { MovieViewModel(get()) }
}

val apiModule = listOf(ApiModule)
val viewModelModule = listOf(ViewModelsModule)