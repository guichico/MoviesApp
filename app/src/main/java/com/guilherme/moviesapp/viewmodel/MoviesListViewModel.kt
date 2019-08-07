package com.guilherme.moviesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.guilherme.moviesapp.api.MovieApi
import com.guilherme.moviesapp.api.NetworkState
import com.guilherme.moviesapp.datasource.SearchMoviesDataSourceFactory
import com.guilherme.moviesapp.datasource.PopularMoviesDataSource
import com.guilherme.moviesapp.datasource.PopularMoviesDataSourceFactory
import com.guilherme.moviesapp.model.Movie
import io.reactivex.disposables.CompositeDisposable

class MoviesListViewModel(private val movieApi: MovieApi) : ViewModel() {
    private var popularMoviesDataSourceFactory: PopularMoviesDataSourceFactory
    private var searchMoviesDataSourceFactory: SearchMoviesDataSourceFactory

    var popularMovies: LiveData<PagedList<Movie>>
    var searchedMovies: LiveData<PagedList<Movie>>

    private val compositeDisposable = CompositeDisposable()

    val loadingVisibility = MutableLiveData<Boolean>()
    val isSearching = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()

    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setInitialLoadSizeHint(20)
        .setPageSize(20)
        .build()

    init {
        isSearching.postValue(false)
        loadingVisibility.postValue(true)
        message.postValue("")

        popularMoviesDataSourceFactory = PopularMoviesDataSourceFactory(compositeDisposable, movieApi)
        popularMovies = LivePagedListBuilder(popularMoviesDataSourceFactory, config).build()

        searchMoviesDataSourceFactory = SearchMoviesDataSourceFactory("", compositeDisposable, movieApi)
        searchedMovies = LivePagedListBuilder(searchMoviesDataSourceFactory, config).build()
    }

    private fun refreshMovies() {
        if (isSearching.value!!)
            searchMoviesDataSourceFactory.moviesDataSourceLiveData.value?.invalidate()
        else
            popularMoviesDataSourceFactory.popularMoviesDataSourceLiveData.value?.invalidate()
    }

    fun searchMovies(query: String) {
        if (query.isNullOrEmpty()) {
            isSearching.postValue(false)
        } else {
            isSearching.postValue(true)

            searchMoviesDataSourceFactory.moviesDataSourceLiveData.value?.query = query
            searchMoviesDataSourceFactory.moviesDataSourceLiveData.value?.invalidate()
        }
    }

    fun getState(): LiveData<NetworkState> = Transformations.switchMap<PopularMoviesDataSource, NetworkState>(
        popularMoviesDataSourceFactory.popularMoviesDataSourceLiveData, PopularMoviesDataSource::state
    )

    fun retry() {
        popularMoviesDataSourceFactory.popularMoviesDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return popularMovies.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    /*fun searchMovie(query: String) {
        movies.postValue(emptyList())
        loadingVisibility.postValue(true)
        message.postValue("")

        if (disposableMovies != null)
            disposableMovies!!.dispose()

        if (query.isNullOrEmpty()) {
            getPopularMovies()
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

    fun getPopularMovies() {
        movies.postValue(emptyList())
        loadingVisibility.postValue(true)
        message.postValue("")

        if (disposablePopularMovies != null)
            disposablePopularMovies!!.dispose()

        disposablePopularMovies = movieApi.getPopularMovies()
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
    }*/
}