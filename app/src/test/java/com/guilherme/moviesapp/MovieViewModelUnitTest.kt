package com.guilherme.moviesapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.guilherme.moviesapp.api.MovieApi
import com.guilherme.moviesapp.model.Movie
import com.guilherme.moviesapp.model.SearchResult
import com.guilherme.moviesapp.viewmodel.MovieViewModel
import io.reactivex.Observable
import okhttp3.Response
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.koin.test.AutoCloseKoinTest
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(Parameterized::class)
class MovieViewModelUnitTest(private val movie: Movie, private val name: String) : AutoCloseKoinTest() {

    @Rule
    @JvmField
    var schedulerRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var movieViewModel: MovieViewModel

    @Mock
    lateinit var movieApi: MovieApi

    companion object {
        var lionKing = Movie(title = "The Lion King")
        var godfather = Movie(title = "The Godfather")

        @JvmStatic
        @Parameterized.Parameters(name = "{1}")
        fun getParameters() = listOf(
            arrayOf(lionKing, lionKing.title),
            arrayOf(godfather, godfather.title)
        )
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        movieViewModel = MovieViewModel(movieApi)
    }

    @Test
    fun getMovieDetailsSuccessTest() {
        Mockito.doReturn(Observable.just(movie)).`when`(movieApi).getMovie(Mockito.anyLong())

        movieViewModel.getMovieDetails(1)

        Assert.assertEquals(movie, movieViewModel.movie.value)
    }

    @Test
    fun getRecommendationsSuccessTest() {
        var searchResult = SearchResult(
            total_results = 1,
            results = listOf(movie)
        )

        Mockito.doReturn(Observable.just(searchResult)).`when`(movieApi).getRecommendations(Mockito.anyLong())

        movieViewModel.getRecommendations(1)

        Assert.assertEquals(searchResult.results, movieViewModel.recommendations.value)
    }

    @Test
    fun getRecommendationsEmptyListTest() {
        var searchResult = SearchResult()

        Mockito.doReturn(Observable.just(searchResult)).`when`(movieApi).getRecommendations(Mockito.anyLong())

        movieViewModel.movie.postValue(movie)
        movieViewModel.getRecommendations(1)

        var expected = String.format("We don't have enough data to suggest any movies based on %s.", movie.title)

        Assert.assertEquals(expected, movieViewModel.message.value)
    }

    @Test
    fun getRecommendationsErrorTest() {
        Mockito.doReturn(Observable.error<Response>(Exception())).`when`(movieApi).getRecommendations(Mockito.anyLong())

        movieViewModel.getRecommendations(1)

        Assert.assertEquals("Error to load recommendations, tap to try again", movieViewModel.message.value)
    }


}