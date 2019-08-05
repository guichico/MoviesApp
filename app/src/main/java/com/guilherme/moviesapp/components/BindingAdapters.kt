package com.guilherme.moviesapp.components

import android.graphics.Typeface
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.guilherme.moviesapp.R
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapters {

    private val locale = Locale("pt", "BR")

    @JvmStatic
    @BindingAdapter("year")
    fun yearFormat(textView: TextView, value: String) {
        if (!value.isNullOrEmpty()) {
            val format = SimpleDateFormat("yyyy", locale)

            var movieDate = format.parse(value)

            var calendar = Calendar.getInstance(locale)
            calendar.time = movieDate

            var color: Int
            var typeface: Typeface
            if (calendar.get(Calendar.YEAR) == Calendar.getInstance(locale).get(Calendar.YEAR)) {
                color = R.color.red
                typeface = Typeface.DEFAULT_BOLD
            } else {
                color = R.color.gray
                typeface = Typeface.DEFAULT
            }

            textView.typeface = typeface
            textView.setTextColor(textView.context.resources.getColor(color))
            textView.text = format.format(movieDate)
        }
    }
}