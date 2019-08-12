package com.guilherme.moviesapp.model

import java.io.Serializable

data class VideoResult(
    val id: Long = 0,
    val results: List<Video> = emptyList()
) : Serializable