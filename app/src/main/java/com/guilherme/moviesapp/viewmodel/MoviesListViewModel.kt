package com.guilherme.moviesapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guilherme.moviesapp.api.MovieApi
import com.guilherme.moviesapp.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MoviesListViewModel(private val movieApi: MovieApi) : ViewModel() {
    val movies = MutableLiveData<List<Movie>>()
    val loadingVisibility = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()

    private var disposableMovies: Disposable? = null

    fun searchMovie(query: String) {
        movies.postValue(emptyList())
        loadingVisibility.postValue(true)
        message.postValue("")

        if (disposableMovies != null)
            disposableMovies!!.dispose()

        if (query.isNullOrEmpty()) {
            message.postValue("empty list")
            loadingVisibility.postValue(false)
        } else {
            disposableMovies = movieApi.searchMovies(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ searchResult ->
                    loadingVisibility.postValue(false)

                    if (searchResult.total_results > 0)
                        movies.postValue(searchResult.results)
                    else message.postValue("empty list")
                }, {
                    message.postValue("error on load")
                    loadingVisibility.postValue(false)
                })
        }
    }
}