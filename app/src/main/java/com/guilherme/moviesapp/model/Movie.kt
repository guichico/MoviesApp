package com.guilherme.moviesapp.model

import java.io.Serializable

data class Movie(
    val id: Long,
    val vote_count: Int,
    val video: Boolean,
    val vote_average: Double,
    val title: String,
    val popularity: Double,
    val poster_path: String,
    val original_language: String,
    val original_title: String,
    val backdrop_path: String,
    val adult: Boolean,
    val overview: String,
    val release_date: String,
    val revenue: Int,
    val runtime: Int
) : Serializable {
    constructor() : this(
        0,
        0,
        false,
        0.0,
        "",
        0.0,
        "",
        "",
        "",
        "",
        false,
        "",
        "",
        0,
        0
    )
}