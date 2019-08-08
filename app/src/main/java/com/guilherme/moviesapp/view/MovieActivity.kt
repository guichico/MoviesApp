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
    private lateinit var binding: ActivityMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.viewModel = movieViewModel
        binding.lifecycleOwner = this

        val movie = intent.getSerializableExtra("movie") as Movie

        movieViewModel.movie.postValue(movie)
        movieViewModel.getMovieDetails(movie.id)

        setRecommendations(movie.id)

        binding.executePendingBindings()
    }

    private fun setRecommendations(movieId: Long) {
        binding.vpRecommendations.pageMargin = 40
        movieViewModel.recommendations.observe(this,
            Observer { movies ->
                binding.vpRecommendations.adapter = RecommendationsAdapter(movies)
            })

        movieViewModel.getRecommendations(movieId)
    }
}