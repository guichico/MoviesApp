package com.guilherme.moviesapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guilherme.moviesapp.api.MovieApi
import com.guilherme.moviesapp.model.Movie
import com.guilherme.moviesapp.model.Video
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MovieViewModel(private val movieApi: MovieApi) : ViewModel() {
    val movie = MutableLiveData<Movie>()
    val recommendations = MutableLiveData<List<Movie>>()

    val trailerKey = MutableLiveData<String>()
    val videos = MutableLiveData<List<Video>>()

    val loadingVisibility = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()
    val hasTrailer = MutableLiveData<Boolean>()

    private var disposableMovie: Disposable? = null
    private var disposableVideos: Disposable? = null
    private var disposableRecommendations: Disposable? = null

    fun getMovieDetails(movieId: Long) {
        if (disposableMovie != null)
            disposableMovie!!.dispose()

        disposableMovie = movieApi.getMovie(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { movie.postValue(it) }
    }

    fun getVideos(movieId: Long) {
        if (disposableVideos != null)
            disposableVideos!!.dispose()

        disposableVideos = movieApi.getVideos(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                var vs = it.results

                getVideoTrailer(vs)

                videos.postValue(vs)
            }, {

            })
    }

    private fun getVideoTrailer(videos: List<Video>): Video? {
        if (!videos.isNullOrEmpty()) {
            hasTrailer.postValue(true)

            videos.forEach {
                if (it.type == "Trailer" && it.site == "YouTube") {
                    trailerKey.postValue(it.key)
                    return it
                }
            }
        }
        return null
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

                if (it.total_results == 0)
                    message.postValue(
                        String.format(
                            "We don't have enough data to suggest any movies based on %s.",
                            movie.value?.title
                        )
                    )
            }, {
                loadingVisibility.postValue(false)
                message.postValue("Error to load recommendations, tap to try again")
            })
    }
}