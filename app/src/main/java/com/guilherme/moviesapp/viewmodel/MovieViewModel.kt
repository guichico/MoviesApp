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
    val recommendations = MutableLiveData<List<Movie>>()

    val loadingVisibility = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()

    private var disposableMovie: Disposable? = null
    private var disposableRecommendations: Disposable? = null

    fun getMovieDetails(movieId: Long) {
        if (disposableMovie != null)
            disposableMovie!!.dispose()

        disposableMovie = movieApi.getMovie(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { movie.postValue(it) }
    }

    fun getRecommendations(movieId: Long) {
        loadingVisibility.postValue(true)
        message.postValue("")

        if (disposableRecommendations != null)
            disposableRecommendations!!.dispose()

        disposableRecommendations = movieApi.getRecommendations(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                loadingVisibility.postValue(false)
                recommendations.postValue(it.results)
            }, {
                loadingVisibility.postValue(false)
                message.postValue("error to load recommendations")
            })
    }
}