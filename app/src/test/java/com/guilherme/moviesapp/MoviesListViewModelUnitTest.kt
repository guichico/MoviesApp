package com.guilherme.moviesapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.guilherme.moviesapp.api.MovieApi
import com.guilherme.moviesapp.model.SearchResult
import com.guilherme.moviesapp.viewmodel.MoviesListViewModel
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MoviesListViewModelUnitTest {

    @Rule
    @JvmField
    var schedulerRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var moviesListViewModel: MoviesListViewModel

    @Mock
    lateinit var movieApi: MovieApi

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        moviesListViewModel = MoviesListViewModel(movieApi)
    }

    @Test
    fun searchMovieSuccessTest() {
        val searchResult = SearchResult()
        var query = "lion king"

        Mockito.doReturn(Observable.just(searchResult)).`when`(movieApi).searchMovies(query)

        moviesListViewModel.searchMovie(query)

        assertEquals(0, moviesListViewModel.movies.value!!.size)
    }

    @Test
    fun getPopularMoviesSuccessTest() {
        val searchResult = SearchResult()

        Mockito.doReturn(Observable.just(searchResult)).`when`(movieApi).getPopularMovies()

        moviesListViewModel.getPopularMovies()

        assertEquals(0, moviesListViewModel.movies.value!!.size)
    }
}
