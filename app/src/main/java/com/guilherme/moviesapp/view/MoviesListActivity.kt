package com.guilherme.moviesapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.guilherme.moviesapp.R
import com.guilherme.moviesapp.components.NpaGridLayoutManager
import com.guilherme.moviesapp.components.SpaceItemDecoration
import com.guilherme.moviesapp.databinding.ActivityMoviesBinding
import com.guilherme.moviesapp.view.adapters.MoviesAdapter
import com.guilherme.moviesapp.viewmodel.MoviesListViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MoviesListActivity : AppCompatActivity() {

    private val moviesViewModel: MoviesListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMoviesBinding = DataBindingUtil.setContentView(this, R.layout.activity_movies)
        binding.viewModel = moviesViewModel
        binding.lifecycleOwner = this

        setMoviesList(binding)
        //setErrorObserver(binding)

        moviesViewModel.showSearchResults("")

        binding.searchMovie.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (moviesViewModel.showSearchResults(newText))
                    (binding.rvMovies.adapter as? MoviesAdapter)?.submitList(null)

                return false
            }
        }
        )

        binding.executePendingBindings()
    }

    private fun setMoviesList(binding: ActivityMoviesBinding) {
        binding.rvMovies.layoutManager = NpaGridLayoutManager(this, 3)
        binding.rvMovies.addItemDecoration(SpaceItemDecoration(3, 40))

        val adapter = MoviesAdapter { moviesViewModel.retry() }
        binding.rvMovies.adapter = adapter
        moviesViewModel.searchedMovies.observe(this, Observer(adapter::submitList))
    }

    /*private fun setErrorObserver(binding: ActivityMoviesBinding) {
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
    }*/
}
