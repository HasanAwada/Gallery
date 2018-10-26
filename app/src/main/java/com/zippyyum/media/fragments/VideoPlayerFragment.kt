package com.zippyyum.media.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util

import com.zippyyum.media.R
import com.zippyyum.media.model.MediaItem
import com.zippyyum.media.utilities.Helper
import kotlinx.android.synthetic.main.fragment_video_player.*
import java.io.File

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * Use the [VideoPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoPlayerFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mediaItem: MediaItem? = null

    private val bandwidthMeter by lazy {
        DefaultBandwidthMeter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mediaItem = arguments!!.getSerializable(MEDIA_ITEM) as MediaItem
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaItem?.let {
            playVideo(pvMedia, mediaItem!!)
        }
    }

    private fun playVideo(viewItem: PlayerView, mediaItem: MediaItem) {
        var mediaSource: MediaSource? = null
        val localVideo = checkVideoLocally(mediaItem.name)
        context?.let { context ->
            localVideo?.let {
                val uri = localVideo?.absolutePath
                Uri.fromFile(localVideo)?.let { uri ->
                    mediaSource = buildMediaSource(uri)
                }
            } ?: run {
                Helper.downloadVideoToMemory(context, mediaItem.url, mediaItem.name)
                Uri.parse(mediaItem.url)?.let { uri ->
                    mediaSource = buildExtractorMediaSource(uri)
                }
            }

            mediaSource.let {
                val player = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
                viewItem.player = player
                player.prepare(mediaSource)

            }
        }
    }

    private fun checkVideoLocally(fileName: String): File? {
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(directory, "$fileName.mp4")
        if (file.exists()) {
            return file
        }
        return null
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory = DefaultDataSourceFactory(context, Helper.getApplicationName(context!!))
        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
        return mediaSource.createMediaSource(uri)
    }

    private fun buildExtractorMediaSource(uri: Uri): ExtractorMediaSource {
        val dataSourceFactory = DefaultHttpDataSourceFactory(Helper.getApplicationName(context!!), bandwidthMeter)
        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
        return mediaSource.createMediaSource(uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val MEDIA_ITEM = "MEDIA_ITEM"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment VideoPlayerFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(mediaItem: MediaItem): VideoPlayerFragment {
            val fragment = VideoPlayerFragment()
            val args = Bundle()
            args.putSerializable(MEDIA_ITEM, mediaItem)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(): VideoPlayerFragment {
            return VideoPlayerFragment()
        }
    }
}// Required empty public constructor
