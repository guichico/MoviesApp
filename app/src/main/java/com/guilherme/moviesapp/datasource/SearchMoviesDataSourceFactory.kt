package com.guilherme.moviesapp.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.guilherme.moviesapp.api.MovieApi
import com.guilherme.moviesapp.model.Movie
import io.reactivex.disposables.CompositeDisposable

class SearchMoviesDataSourceFactory(
    private val query: String,
    private val compositeDisposable: CompositeDisposable,
    private val movieApi: MovieApi
) : DataSource.Factory<Int, Movie>() {

    val moviesDataSourceLiveData = MutableLiveData<SearchMoviesDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val moviesDataSource = SearchMoviesDataSource(query, movieApi, compositeDisposable)
        moviesDataSourceLiveData.postValue(moviesDataSource)
        return moviesDataSource
    }
}