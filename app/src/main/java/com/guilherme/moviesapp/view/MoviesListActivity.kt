package com.guilherme.moviesapp.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guilherme.moviesapp.R
import com.guilherme.moviesapp.api.NetworkState
import com.guilherme.moviesapp.components.SpaceItemDecoration
import com.guilherme.moviesapp.databinding.ActivityMoviesBinding
import com.guilherme.moviesapp.model.Movie
import com.guilherme.moviesapp.view.adapters.MoviesAdapter
import com.guilherme.moviesapp.viewmodel.MoviesListViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MoviesListActivity : AppCompatActivity() {

    private val moviesViewModel: MoviesListViewModel by viewModel()
    private lateinit var popularMoviesAdapter: MoviesAdapter
    private lateinit var searchedMoviesAdapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMoviesBinding = DataBindingUtil.setContentView(this, R.layout.activity_movies)
        binding.viewModel = moviesViewModel
        binding.lifecycleOwner = this

        setPopularMoviesList(binding)
        setSearchedMoviesList(binding)
        setErrorObserver(binding)

        binding.searchMovie.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                moviesViewModel.searchMovies(newText!!)
                return false
            }
        }
        )

        binding.executePendingBindings()
    }

    private fun setPopularMoviesList(binding: ActivityMoviesBinding) {
        popularMoviesAdapter = MoviesAdapter { moviesViewModel.retry() }
        setMoviesList(binding.rvPopularMovies, popularMoviesAdapter, moviesViewModel.popularMovies)
    }

    private fun setSearchedMoviesList(binding: ActivityMoviesBinding) {
        searchedMoviesAdapter = MoviesAdapter { moviesViewModel.retry() }
        setMoviesList(binding.rvSearchedMovies, searchedMoviesAdapter, moviesViewModel.searchedMovies)
    }

    private fun setMoviesList(
        recyclerView: RecyclerView,
        adapter: MoviesAdapter,
        movies: LiveData<PagedList<Movie>>
    ) {
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.addItemDecoration(SpaceItemDecoration(3, 40))

        recyclerView.adapter = adapter
        movies.observe(this,
            Observer { movies ->
                adapter.submitList(movies)
            })
    }

    private fun setErrorObserver(binding: ActivityMoviesBinding) {
        binding.txtError.setOnClickListener { moviesViewModel.retry() }
        moviesViewModel.getState().observe(this, Observer { state ->
            binding.progressBar.visibility =
                if (moviesViewModel.listIsEmpty() && state == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.txtError.visibility =
                if (moviesViewModel.listIsEmpty() && state == NetworkState.ERROR) View.VISIBLE else View.GONE
            if (!moviesViewModel.listIsEmpty()) {
                popularMoviesAdapter.setState(state ?: NetworkState.DONE)
            }
        })
    }
}
