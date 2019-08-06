package com.guilherme.moviesapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guilherme.moviesapp.api.MovieApi
import com.guilherme.moviesapp.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MovieViewModel(private val movieApi: MovieApi) : ViewModel() {
    val movie = MutableLiveData<Movie>()
    val loadingVisibility = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()

    private var disposableMovie: Disposable? = null

    fun getMovie(movieId: Long) {
        disposableMovie = movieApi.getMovie(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                movie.postValue(it)
            }, {

            })
    }
}