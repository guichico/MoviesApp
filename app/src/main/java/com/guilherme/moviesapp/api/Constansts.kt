package com.guilherme.moviesapp.api

val api_key = "83d01f18538cb7a275147492f84c3698"
val api_path = "https://api.themoviedb.org/3/"

fun getImgPath(size: Int): String {
    return String.format("%s%d/", "https://image.tmdb.org/t/p/w", size)
}