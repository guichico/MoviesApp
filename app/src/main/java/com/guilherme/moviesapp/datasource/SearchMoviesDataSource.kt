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

class SearchMoviesDataSource(
    private val query: String,
    private val movieApi: MovieApi,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Movie>() {

    val state = MutableLiveData<NetworkState>()
    val initial = MutableLiveData<NetworkState>()

    private var retryCompletable: Completable? = null

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {
        state.postValue(NetworkState.LOADING)
        initial.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            movieApi.searchMovies(query, 1)
                .subscribe({
                    if (it.total_results == 0) {
                        state.postValue(NetworkState.error("There are no movies that matched your query."))
                        initial.postValue(NetworkState.error("There are no movies that matched your query."))
                    } else {
                        state.postValue(NetworkState.DONE)
                        initial.postValue(NetworkState.DONE)
                    }

                    callback.onResult(it.results, null, 2)
                }, {
                    state.postValue(NetworkState.error(it.message))
                    initial.postValue(NetworkState.error(it.message))

                    setRetry(Action { loadInitial(params, callback) })
                })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        state.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            movieApi.searchMovies(query, params.key)
                .subscribe({
                    state.postValue(NetworkState.DONE)
                    callback.onResult(it.results, params.key + 1)
                }, {
                    state.postValue(NetworkState.error(it.message))
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