package com.guilherme.moviesapp.api

import com.guilherme.moviesapp.model.SearchResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("search/movie")
    fun searchMovies(@Query("query") query: String) : Observable<SearchResult>
}