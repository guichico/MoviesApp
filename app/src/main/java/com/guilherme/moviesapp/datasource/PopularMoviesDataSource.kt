package com.guilherme.moviesapp.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.guilherme.moviesapp.api.MovieApi
import com.guilherme.moviesapp.api.NetworkState
import com.guilherme.moviesapp.model.Movie
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class PopularMoviesDataSource(
    private val movieApi: MovieApi,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Movie>() {

    val state: MutableLiveData<NetworkState> = MutableLiveData()
    val initial: MutableLiveData<NetworkState> = MutableLiveData()

    private var retryCompletable: Completable? = null

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {
        state.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            movieApi.getPopularMovies(1)
                .subscribe({
                    state.postValue(NetworkState.DONE)
                    callback.onResult(it.results, null, 2)
                }, {
                    state.postValue(NetworkState.ERROR)
                    setRetry(Action { loadInitial(params, callback) })
                })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        state.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            movieApi.getPopularMovies(params.key)
                .subscribe({
                    state.postValue(NetworkState.DONE)
                    callback.onResult(it.results, params.key + 1)
                }, {
                    state.postValue(NetworkState.ERROR)
                    setRetry(Action { loadAfter(params, callback) })
                })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(
                retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }
}