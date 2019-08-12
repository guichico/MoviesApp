package com.guilherme.moviesapp

object Constants {

    const val api_key = "83d01f18538cb7a275147492f84c3698"
    const val api_path = "https://api.themoviedb.org/3/"
    const val google_api_key = "AIzaSyCUQEXTvFl-cGKGGPU_klG9ae7I4XDsmuA"

    @JvmStatic
    fun getImgPath(size: Int): String {
        return String.format("%s%d/", "https://image.tmdb.org/t/p/w", size)
    }
}