package com.guilherme.moviesapp.model

import java.io.Serializable

data class SearchResult(
    val page: Int = 0,
    val total_results: Int = 0,
    val total_pages: Int = 0,
    val results: List<Movie> = emptyList()
) : Serializable