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
    val detailsVisibility = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()

    private var disposableMovie: Disposable? = null

    fun getMovie(movieId: Long) {
        loadingVisibility.postValue(true)
        detailsVisibility.postValue(false)
        message.postValue("")

        if (disposableMovie != null)
            disposableMovie!!.dispose()

        disposableMovie = movieApi.getMovie(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                loadingVisibility.postValue(false)
                detailsVisibility.postValue(true)
                movie.postValue(it)
            }, {
                loadingVisibility.postValue(false)
                message.postValue("error to load details data")
            })
    }
}