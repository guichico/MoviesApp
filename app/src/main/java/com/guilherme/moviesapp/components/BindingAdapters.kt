package com.guilherme.moviesapp.components

import android.graphics.Typeface
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.guilherme.moviesapp.R
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapters {

    private val locale = Locale("en", "US")

    @JvmStatic
    @BindingAdapter("year")
    fun yearFormat(textView: TextView, year: String?) {
        if (!year.isNullOrEmpty()) {
            val format = SimpleDateFormat("yyyy", locale)

            var movieDate = format.parse(year)

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

    @JvmStatic
    @BindingAdapter("value")
    fun currencyFormat(textView: TextView, value: Long) {
        if (value == 0L) {
            textView.text = "-"
        } else {
            val nf = NumberFormat.getCurrencyInstance(locale)
            val pattern = (nf as DecimalFormat).toPattern()
            val newPattern = pattern.replace("\u00A4", "").trim { it <= ' ' }
            val newFormat = DecimalFormat(newPattern)
            textView.text = String.format("$%s", newFormat.format(value))
        }
    }

    @JvmStatic
    @BindingAdapter("imgUrl")
    fun loadImage(imageView: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) {
            Picasso.get()
                .load(url)
                .placeholder(R.color.grayLight)
                .fit()
                .centerCrop()
                .into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("minutes")
    fun convertToHourAndMinutes(textView: TextView, minutes: Int) {
        if (minutes == 0) {
            textView.text = "-"
        } else {
            val hours = minutes / 60
            val minutes = minutes % 60
            textView.text = String.format("%dh%02dm", hours, minutes)
        }
    }
}