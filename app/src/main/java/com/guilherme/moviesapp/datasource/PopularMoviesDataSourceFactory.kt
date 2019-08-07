package com.guilherme.moviesapp.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.guilherme.moviesapp.api.MovieApi
import com.guilherme.moviesapp.model.Movie
import io.reactivex.disposables.CompositeDisposable

class PopularMoviesDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val movieApi: MovieApi
) : DataSource.Factory<Int, Movie>() {

    val popularMoviesDataSourceLiveData = MutableLiveData<PopularMoviesDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val popularMoviesDataSource = PopularMoviesDataSource(movieApi, compositeDisposable)
        popularMoviesDataSourceLiveData.postValue(popularMoviesDataSource)
        return popularMoviesDataSource
    }
}