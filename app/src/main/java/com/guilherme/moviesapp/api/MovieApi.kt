package com.guilherme.moviesapp.api

import com.guilherme.moviesapp.model.Movie
import com.guilherme.moviesapp.model.SearchResult
import com.guilherme.moviesapp.model.VideoResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int): Observable<SearchResult>

    @GET("search/movie")
    fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Observable<SearchResult>

    @GET("movie/{movie_id}")
    fun getMovie(@Path("movie_id") movieId: Long): Observable<Movie>

    @GET("movie/{movie_id}/recommendations")
    fun getRecommendations(@Path("movie_id") movieId: Long): Observable<SearchResult>

    @GET("movie/{movie_id}/videos")
    fun getVideos(@Path("movie_id") movieId: Long): Observable<VideoResult>
}