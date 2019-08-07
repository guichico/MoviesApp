package com.guilherme.moviesapp.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guilherme.moviesapp.R
import com.guilherme.moviesapp.api.NetworkState
import kotlinx.android.synthetic.main.row_footer.view.*

class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(status: NetworkState?) {
        itemView.progress_bar.visibility = if (status == NetworkState.LOADING) View.VISIBLE else View.INVISIBLE
        itemView.txt_error.visibility = if (status == NetworkState.ERROR) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        fun create(retry: () -> Unit, parent: ViewGroup): FooterViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_footer, parent, false)
            view.txt_error.setOnClickListener { retry() }
            return FooterViewHolder(view)
        }
    }
}