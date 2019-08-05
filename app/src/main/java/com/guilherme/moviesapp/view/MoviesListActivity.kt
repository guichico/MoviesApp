package com.guilherme.moviesapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.SearchViewBindingAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.guilherme.moviesapp.R
import com.guilherme.moviesapp.components.SpaceItemDecoration
import com.guilherme.moviesapp.databinding.ActivityMoviesBinding
import com.guilherme.moviesapp.viewmodel.MoviesListViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MoviesListActivity : AppCompatActivity() {

    private val moviesViewModel: MoviesListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMoviesBinding = DataBindingUtil.setContentView(this, R.layout.activity_movies)
        binding.viewModel = moviesViewModel
        binding.lifecycleOwner = this

        binding.rvMovies.layoutManager = GridLayoutManager(this, 3)
        binding.rvMovies.addItemDecoration(SpaceItemDecoration(3, 40))

        moviesViewModel.movies.observe(this,
            Observer { movies ->
                binding.rvMovies.adapter = MoviesAdapter(movies)
            })

        binding.searchMovie.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                moviesViewModel.searchMovie(newText!!)
                return false
            }
        }
        )

        binding.executePendingBindings()
    }
}
