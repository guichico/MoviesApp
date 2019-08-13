package com.guilherme.moviesapp

object Constants {

    const val api_key = "1a9af0a11f329fb61de3fa62de0f502d"
    const val api_path = "https://api.themoviedb.org/3/"
    const val share_path = "https://www.themoviedb.org/"
    const val google_api_key = "AIzaSyCUQEXTvFl-cGKGGPU_klG9ae7I4XDsmuA"

    @JvmStatic
    fun getImgPath(size: Int): String {
        return String.format("%s%d/", "https://image.tmdb.org/t/p/w", size)
    }
}