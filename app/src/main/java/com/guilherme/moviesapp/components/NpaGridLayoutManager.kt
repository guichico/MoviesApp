package com.guilherme.moviesapp.components

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

class NpaGridLayoutManager : GridLayoutManager {

    constructor(context: Context?, spanCount: Int) : super(context, spanCount)

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}