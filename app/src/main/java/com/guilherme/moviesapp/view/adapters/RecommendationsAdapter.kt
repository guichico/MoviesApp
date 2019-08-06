package com.guilherme.moviesapp.view.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.guilherme.moviesapp.R
import com.guilherme.moviesapp.databinding.RowRecommendationBinding
import com.guilherme.moviesapp.model.Movie
import com.guilherme.moviesapp.view.MovieActivity

class RecommendationsAdapter(private val movies: List<Movie>) : PagerAdapter() {

    override fun getCount(): Int = movies.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        val binding: RowRecommendationBinding =
            DataBindingUtil.inflate(inflater, R.layout.row_recommendation, container, false)

        val movie = movies[position]

        binding.movie = movie
        binding.root.setOnClickListener {
            val context = binding.root.context

            val intent = Intent(context, MovieActivity::class.java)
            intent.putExtra("movie", movie)

            context.startActivity(intent)
        }

        container.addView(binding.root)

        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}