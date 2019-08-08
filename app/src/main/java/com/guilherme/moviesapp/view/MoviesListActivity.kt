package com.guilherme.moviesapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.guilherme.moviesapp.R
import com.guilherme.moviesapp.api.NetworkState
import com.guilherme.moviesapp.components.NpaGridLayoutManager
import com.guilherme.moviesapp.components.SpaceItemDecoration
import com.guilherme.moviesapp.databinding.ActivityMoviesBinding
import com.guilherme.moviesapp.view.adapters.MoviesAdapter
import com.guilherme.moviesapp.viewmodel.MoviesListViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MoviesListActivity : AppCompatActivity(), SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {

    private val moviesViewModel: MoviesListViewModel by viewModel()
    private lateinit var binding: ActivityMoviesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_movies)
        binding.viewModel = moviesViewModel
        binding.lifecycleOwner = this

        setMoviesList()
        setObservers()

        binding.txtError.setOnClickListener { moviesViewModel.retry() }
        moviesViewModel.showSearchResults("")

        binding.searchMovie.setOnQueryTextListener(this)
        binding.srMovies.setOnRefreshListener(this)

        binding.executePendingBindings()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (moviesViewModel.showSearchResults(newText))
            (binding.rvMovies.adapter as? MoviesAdapter)?.submitList(null)

        return false
    }

    override fun onRefresh() {
        moviesViewModel.refresh()
    }

    private fun setMoviesList() {
        binding.rvMovies.layoutManager = NpaGridLayoutManager(this, 3)
        binding.rvMovies.addItemDecoration(SpaceItemDecoration(3, 40))

        val adapter = MoviesAdapter { moviesViewModel.retry() }
        binding.rvMovies.adapter = adapter
        moviesViewModel.searchedMovies.observe(this, Observer(adapter::submitList))
    }

    private fun setObservers() {
        moviesViewModel.networkState.observe(this, Observer {
            (binding.rvMovies.adapter as? MoviesAdapter)?.setState(it)

            if (it != NetworkState.LOADING)
                moviesViewModel.isFirstLoading.postValue(false)
        })
        moviesViewModel.refreshState.observe(this, Observer {
            binding.srMovies.isRefreshing = it == NetworkState.LOADING
        })
    }
}
