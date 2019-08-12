package com.guilherme.moviesapp.model

import java.io.Serializable

data class Video(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val type: String
) : Serializable