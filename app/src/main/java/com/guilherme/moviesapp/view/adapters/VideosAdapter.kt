package com.guilherme.moviesapp.view.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.guilherme.moviesapp.R
import com.guilherme.moviesapp.databinding.RowVideoBinding
import com.guilherme.moviesapp.model.Video
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeStandalonePlayer
import com.guilherme.moviesapp.Constants

class VideosAdapter(private val context: Activity, private val videos: List<Video>) : PagerAdapter() {

    private var thumbnailViewToLoaderMap: Map<YouTubeThumbnailView, YouTubeThumbnailLoader>? = null
    private var thumbnailListener: ThumbnailListener? = null

    init {
        thumbnailViewToLoaderMap = HashMap()
        thumbnailListener = ThumbnailListener()
    }

    override fun getCount(): Int = videos.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        val binding: RowVideoBinding =
            DataBindingUtil.inflate(inflater, R.layout.row_video, container, false)

        var video = videos[position]
        binding.video = video

        binding.tbvVideo.initialize(Constants.google_api_key, thumbnailListener)
        binding.tbvVideo.tag = video.key
        val loader = thumbnailViewToLoaderMap?.get(binding.tbvVideo)
        loader?.setVideo(video.key)

        binding.tbvVideo.setOnClickListener {
            val intent = YouTubeStandalonePlayer.createVideoIntent(
                context, Constants.google_api_key,
                video.key
            )
            context.startActivity(intent)
        }

        container.addView(binding.root)

        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

    inner class ThumbnailListener : YouTubeThumbnailView.OnInitializedListener,
        YouTubeThumbnailLoader.OnThumbnailLoadedListener {

        override fun onInitializationSuccess(view: YouTubeThumbnailView?, loader: YouTubeThumbnailLoader?) {
            loader!!.setOnThumbnailLoadedListener(this)
            thumbnailViewToLoaderMap?.plus(Pair(view, loader))
            view!!.setImageResource(R.color.grayLight)
            val videoId = view.tag as String
            loader.setVideo(videoId)
        }

        override fun onInitializationFailure(view: YouTubeThumbnailView?, loader: YouTubeInitializationResult?) {
            view!!.setImageResource(R.color.grayLight);
        }

        override fun onThumbnailLoaded(view: YouTubeThumbnailView?, loader: String?) {
        }

        override fun onThumbnailError(view: YouTubeThumbnailView?, loader: YouTubeThumbnailLoader.ErrorReason?) {
            view!!.setImageResource(R.color.grayLight);
        }
    }
}