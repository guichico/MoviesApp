package com.guilherme.moviesapp.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.youtube.player.YouTubeStandalonePlayer
import com.guilherme.moviesapp.Constants
import com.guilherme.moviesapp.R
import com.guilherme.moviesapp.databinding.ActivityMovieBinding
import com.guilherme.moviesapp.model.Movie
import com.guilherme.moviesapp.view.adapters.RecommendationsAdapter
import com.guilherme.moviesapp.view.adapters.VideosAdapter
import com.guilherme.moviesapp.viewmodel.MovieViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import android.content.Intent

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
        setVideos(movie.id)

        binding.btnTrailer.setOnClickListener { playVideo() }

        binding.executePendingBindings()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_movie, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> share()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun share() {
        val movie = movieViewModel.movie.value
        movie?.let {
            // the link could be shorter, but I am using the same as the website
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, getMovieShareLink(it))
            intent.type = "text/plain"
            startActivity(intent)
        }
    }

    private fun getMovieShareLink(movie: Movie): String {
        var title = movie.title.toLowerCase()
        title = title.replace(":", "")
        title = title.replace("-", "")
        title = title.replace("[^a-zA-Z0-9]+", "")
        title = title.replace(" ", "-")

        return Constants.share_path + "movie/" + movie.id + "-" + title
    }

    private fun playVideo() {
        val intent = YouTubeStandalonePlayer.createVideoIntent(
            this, Constants.google_api_key,
            movieViewModel.trailerKey.value
        )
        startActivity(intent)
    }

    private fun setRecommendations(movieId: Long) {
        binding.vpRecommendations.pageMargin = 40
        movieViewModel.recommendations.observe(this,
            Observer { movies ->
                binding.vpRecommendations.adapter = RecommendationsAdapter(movies)
            })

        movieViewModel.getRecommendations(movieId)
    }

    private fun setVideos(movieId: Long) {
        binding.vpVideos.pageMargin = 40
        movieViewModel.videos.observe(this,
            Observer { videos ->
                binding.vpVideos.adapter = VideosAdapter(this, videos)
            })

        movieViewModel.getVideos(movieId)
    }
}