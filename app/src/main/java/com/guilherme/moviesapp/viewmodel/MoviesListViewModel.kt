package com.guilherme.moviesapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.guilherme.moviesapp.api.MovieApi
import com.guilherme.moviesapp.datasource.Listing
import com.guilherme.moviesapp.datasource.PopularMoviesDataSourceFactory
import com.guilherme.moviesapp.datasource.SearchMoviesDataSourceFactory
import com.guilherme.moviesapp.model.Movie
import io.reactivex.disposables.CompositeDisposable

class MoviesListViewModel(private val movieApi: MovieApi) : ViewModel() {
    private val searchQuery = MutableLiveData<String>()
    private val itemResult = Transformations.map(searchQuery) {
        if (it.isNullOrEmpty()) getPopularMovies()
        else searchMovies(it)
    }
    val searchedMovies = Transformations.switchMap(itemResult) { it.pagedList }!!
    val networkState = Transformations.switchMap(itemResult) { it.networkState }!!
    val refreshState = Transformations.switchMap(itemResult) { it.refreshState }!!

    private val popularCompositeDisposable = CompositeDisposable()
    private val searchCompositeDisposable = CompositeDisposable()

    val isFirstLoading = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()

    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setInitialLoadSizeHint(20)
        .setPageSize(20)
        .build()

    init {
        isFirstLoading.postValue(true)
        message.postValue("")
    }

    private fun getPopularMovies(): Listing<Movie> {
        val factory = PopularMoviesDataSourceFactory(popularCompositeDisposable, movieApi)

        val livePagedList = LivePagedListBuilder(factory, config).build()

        return Listing(
            pagedList = livePagedList,
            networkState = Transformations.switchMap(factory.popularMoviesDataSourceLiveData) { it.state },
            retry = { factory.popularMoviesDataSourceLiveData.value?.retry() },
            refresh = { factory.popularMoviesDataSourceLiveData.value?.invalidate() },
            refreshState = Transformations.switchMap(factory.popularMoviesDataSourceLiveData) { it.initial })
    }

    private fun searchMovies(query: String): Listing<Movie> {
        val factory = SearchMoviesDataSourceFactory(query, searchCompositeDisposable, movieApi)

        val livePagedList = LivePagedListBuilder(factory, config).build()

        return Listing(
            pagedList = livePagedList,
            networkState = Transformations.switchMap(factory.moviesDataSourceLiveData) { it.state },
            retry = { factory.moviesDataSourceLiveData.value?.retry() },
            refresh = { factory.moviesDataSourceLiveData.value?.invalidate() },
            refreshState = Transformations.switchMap(factory.moviesDataSourceLiveData) { it.initial })
    }

    fun refresh() {
        itemResult.value?.refresh?.invoke()
    }

    fun showSearchResults(query: String?): Boolean {
        if (this.searchQuery.value == query) {
            return false
        }
        this.searchQuery.value = query

        return true
    }

    fun retry() {
        val listing = itemResult?.value
        listing?.retry?.invoke()
    }

    override fun onCleared() {
        super.onCleared()
        popularCompositeDisposable.dispose()
        searchCompositeDisposable.dispose()
    }
}