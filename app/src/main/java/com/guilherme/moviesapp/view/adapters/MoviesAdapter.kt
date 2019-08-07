package com.guilherme.moviesapp.view.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.guilherme.moviesapp.R
import com.guilherme.moviesapp.api.NetworkState
import com.guilherme.moviesapp.components.FooterViewHolder
import com.guilherme.moviesapp.databinding.RowMovieBinding
import com.guilherme.moviesapp.model.Movie
import com.guilherme.moviesapp.view.MovieActivity

class MoviesAdapter(private val retry: () -> Unit) : PagedListAdapter<Movie, RecyclerView.ViewHolder>(diffCallback) {

    private var state = NetworkState.LOADING

    companion object {

        private const val DATA_VIEW_TYPE = 1
        private const val FOOTER_VIEW_TYPE = 2

        private val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) {
            val inflater = LayoutInflater.from(parent.context)
            val binding: RowMovieBinding = DataBindingUtil.inflate(inflater, R.layout.row_movie, parent, false)
            MovieViewHolder(binding)
        } else {
            FooterViewHolder.create(retry, parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as MovieViewHolder).bind(getItem(position))
        else (holder as FooterViewHolder).bind(state)
    }

    override fun getItemCount(): Int = super.getItemCount() + if (hasFooter()) 1 else 0

    override fun getItemViewType(position: Int): Int =
        if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE

    private fun hasFooter(): Boolean =
        super.getItemCount() != 0 && (state == NetworkState.LOADING || state == NetworkState.ERROR)

    fun setState(state: NetworkState) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }

    class MovieViewHolder(private val binding: RowMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie?) {
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