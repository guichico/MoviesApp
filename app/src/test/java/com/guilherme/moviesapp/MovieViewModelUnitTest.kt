package com.guilherme.moviesapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.guilherme.moviesapp.api.MovieApi
import com.guilherme.moviesapp.model.Movie
import com.guilherme.moviesapp.model.SearchResult
import com.guilherme.moviesapp.viewmodel.MovieViewModel
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.AutoCloseKoinTest
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class MovieViewModelUnitTest : AutoCloseKoinTest() {

    @Rule
    @JvmField
    var schedulerRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var movieViewModel: MovieViewModel

    @Mock
    lateinit var movieApi: MovieApi

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        movieViewModel = MovieViewModel(movieApi)
    }

    @Test
    fun getMovieDetailsSuccessTest() {
        var movie = Movie()

        Mockito.doReturn(Observable.just(movie)).`when`(movieApi).getMovie(Mockito.anyLong())

        movieViewModel.getMovieDetails(1)

        Assert.assertEquals(movie, movieViewModel.movie.value)
    }

    @Test
    fun getRecommendationsSuccessTest() {
        var searchResult = SearchResult()

        Mockito.doReturn(Observable.just(searchResult)).`when`(movieApi).getRecommendations(Mockito.anyLong())

        movieViewModel.getRecommendations(1)

        Assert.assertEquals(0, movieViewModel.recommendations.value!!.size)
    }
}