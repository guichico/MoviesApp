package com.guilherme.moviesapp.view.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.guilherme.moviesapp.R
import com.guilherme.moviesapp.api.getImgPath
import com.guilherme.moviesapp.databinding.RowMovieBinding
import com.guilherme.moviesapp.model.Movie
import com.guilherme.moviesapp.view.MovieActivity
import com.squareup.picasso.Picasso

class MoviesAdapter(private var movies: List<Movie>) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: RowMovieBinding = DataBindingUtil.inflate(inflater, R.layout.row_movie, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    class ViewHolder(private val binding: RowMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            Picasso.get()
                .load(getImgPath(200) + movie.poster_path)
                .placeholder(R.color.grayLight)
                .fit()
                .into(binding.imgMovie)

            binding.root.setOnClickListener {
                val context = binding.root.context

                val intent = Intent(context, MovieActivity::class.java)
                intent.putExtra("movie", movie)

                context.startActivity(intent)
            }

            binding.movie = movie
            binding.executePendingBindings()
        }
    }
}