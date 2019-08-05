package com.guilherme.moviesapp.model

import java.io.Serializable

data class SearchResult(
    val page: Int,
    val total_results: Int,
    val total_pages: Int,
    val results: List<Movie>
) : Serializable {
    constructor() : this(
        0,
        0,
        0,
        emptyList()
    )
}