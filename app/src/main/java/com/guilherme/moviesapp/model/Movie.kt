package com.guilherme.moviesapp.model

import java.io.Serializable

data class Movie(
    val id: Long = 0,
    val vote_count: Int = 0,
    val vote_average: Double = 0.0,
    val title: String = "",
    val popularity: Double = 0.0,
    val poster_path: String = "",
    val original_language: String = "",
    val original_title: String = "",
    val backdrop_path: String = "",
    val adult: Boolean = false,
    val overview: String = "",
    val release_date: String = "",
    val budget: Long = 0L,
    val revenue: Long = 0L,
    val runtime: Int = 0,
    val genres: List<Genres> = emptyList()
) : Serializable