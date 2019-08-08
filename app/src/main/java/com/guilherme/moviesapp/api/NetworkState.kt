package com.guilherme.moviesapp.api

enum class Status {
    DONE, LOADING, ERROR
}

data class NetworkState private constructor(
    val status: Status,
    val msg: String? = null
) {
    companion object {
        val DONE = NetworkState(Status.DONE)
        val LOADING = NetworkState(Status.LOADING)
        val ERROR = NetworkState(Status.ERROR)
        fun error(msg: String?) = NetworkState(Status.ERROR, msg)
    }
}