package com.guilherme.moviesapp.components

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(private val spanCount: Int, private val spaceHeight: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        outRect.left = spaceHeight - column * spaceHeight / spanCount
        outRect.right = (column + 1) * spaceHeight / spanCount

        if (position < spanCount)
            outRect.top = spaceHeight

        outRect.bottom = spaceHeight
    }
}