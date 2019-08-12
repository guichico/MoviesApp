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
    val videos = MutableLiveData<List<Video>>()
    val recommendations = MutableLiveData<List<Movie>>()
    val trailerKey = MutableLiveData<String>()

    val hasTrailer = MutableLiveData<Boolean>()

    val loadingVisibilityVideos = MutableLiveData<Boolean>()
    val messageVideos = MutableLiveData<String>()

    val loadingVisibilityRecommendations = MutableLiveData<Boolean>()
    val messageRecommendations = MutableLiveData<String>()

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
        loadingVisibilityVideos.postValue(true)
        messageVideos.postValue("")

        if (disposableVideos != null)
            disposableVideos!!.dispose()

        disposableVideos = movieApi.getVideos(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                loadingVisibilityVideos.postValue(false)

                var vs = it.results

                if (vs.isNullOrEmpty()) {
                    messageRecommendations.postValue("We don't have any video for this movie yet.")
                } else {
                    getVideoTrailer(vs)

                    videos.postValue(vs)
                }
            }, {
                loadingVisibilityVideos.postValue(false)
                messageVideos.postValue("Error to load videos, tap to try again")
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
        loadingVisibilityRecommendations.postValue(true)
        messageRecommendations.postValue("")

        if (disposableRecommendations != null)
            disposableRecommendations!!.dispose()

        disposableRecommendations = movieApi.getRecommendations(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                loadingVisibilityRecommendations.postValue(false)
                recommendations.postValue(it.results)

                if (it.total_results == 0)
                    messageRecommendations.postValue(
                        String.format(
                            "We don't have enough data to suggest any movies based on %s.",
                            movie.value?.title
                        )
                    )
            }, {
                loadingVisibilityRecommendations.postValue(false)
                messageRecommendations.postValue("Error to load recommendations, tap to try again")
            })
    }
}