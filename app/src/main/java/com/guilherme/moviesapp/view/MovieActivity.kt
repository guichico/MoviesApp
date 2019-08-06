package com.guilherme.moviesapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.guilherme.moviesapp.R
import com.guilherme.moviesapp.databinding.ActivityMovieBinding
import com.guilherme.moviesapp.model.Movie
import com.guilherme.moviesapp.view.adapters.RecommendationsAdapter
import com.guilherme.moviesapp.viewmodel.MovieViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MovieActivity : AppCompatActivity() {

    private val movieViewModel: MovieViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMovieBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie)
        val movie = intent.getSerializableExtra("movie") as Movie

        binding.viewModel = movieViewModel
        binding.lifecycleOwner = this

        binding.vpRecommendations.pageMargin = 40

        movieViewModel.movie.postValue(movie)
        movieViewModel.getMovieDetails(movie.id)

        movieViewModel.recommendations.observe(this,
            Observer { movies ->
                binding.vpRecommendations.adapter = RecommendationsAdapter(movies)
            })

        movieViewModel.getRecommendations(movie.id)

        binding.executePendingBindings()
    }
}