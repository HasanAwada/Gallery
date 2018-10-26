package com.zippyyum.media.adapter

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.zippyyum.media.R
import com.zippyyum.media.model.MediaItem
import com.zippyyum.media.utilities.ApiUtils
import com.zippyyum.media.utilities.Helper
import kotlinx.android.synthetic.main.image_layout.view.*
import kotlinx.android.synthetic.main.pdf_layout.view.*
import kotlinx.android.synthetic.main.video_layout.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

/**
 * Created by Hasan.Awada on 10/25/2018.
 */
class MediaViewerAdapter(context: Context, mediaItems: ArrayList<MediaItem>) : Adapter<RecyclerView.ViewHolder>() {

    val TAG = "MediaViewerAdapter"
    private var mMediaItems = mediaItems
    private val context = context
    private var selectedMediaIndex = 0

    private val bandwidthMeter by lazy {
        DefaultBandwidthMeter()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)

        val view = when (viewType) {
            0 -> {
                inflater.inflate(R.layout.image_layout, viewGroup, false)
            }
            1 -> {
                inflater.inflate(R.layout.video_layout, viewGroup, false)
            }
            2 -> {
                inflater.inflate(R.layout.pdf_layout, viewGroup, false)
            }
            else -> {
                inflater.inflate(R.layout.image_layout, viewGroup, false)
            }
        }

        return MediaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mMediaItems.size
    }

    override fun onBindViewHolder(view: RecyclerView.ViewHolder, index: Int) {
        val mediaItem = mMediaItems[index]
        selectedMediaIndex = index
        when (mediaItem.type) {
            MediaItem.MediaType.IMAGE -> Helper.loadImage(context, view.itemView.ivMedia, mediaItem.url)
            MediaItem.MediaType.VIDEO -> playVideo(view.itemView, mediaItem)
            MediaItem.MediaType.PDF -> displayPDF(view.itemView, mediaItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val mediaItem = mMediaItems[position]
        return when (mediaItem.type) {
            MediaItem.MediaType.IMAGE -> 0
            MediaItem.MediaType.VIDEO -> 1
            MediaItem.MediaType.PDF -> 2
        }
    }

    private fun playVideo(viewItem: View, mediaItem: MediaItem) {
        var mediaSource: MediaSource? = null
        val localVideo = checkVideoLocally(mediaItem.name)
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
            player.prepare(mediaSource)
//            ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector()).prepare(mediaSource)
        }
/*
        val player = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
        viewItem.pvMedia.player = player

        val dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, Helper.getApplicationName(context)))

        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(mediaItem.url))

        player.prepare(mediaSource)
*/
    }

    private fun displayPDF(viewItem: View, mediaItem: MediaItem) {
        val file = File(context?.getExternalFilesDir(null).toString() + File.separator + mediaItem.name + ".pdf")
        if (file.exists()) {
            viewItem.pdfvMedia.fromFile(file).pageFitPolicy(FitPolicy.WIDTH)
                    .load()
        } else {
            val retrofitService = ApiUtils.retrofitService
            val call = retrofitService?.getPDFFile()

            call?.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d(TAG, "server contacted and has file")

                    val writtenToDisk = response.body()?.let { content -> Helper.downloadPDFToMemory(context, content, mediaItem.name) }

                    Log.d(TAG, "file download was a success? " + writtenToDisk)
                    val file = File(context?.getExternalFilesDir(null).toString() + File.separator + mediaItem.name + ".pdf")
                    if (file.exists()) {
                        viewItem.pdfvMedia.fromFile(file).pageFitPolicy(FitPolicy.WIDTH)
                                .load()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e(TAG, "error")
                }
            })
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
        val dataSourceFactory = DefaultDataSourceFactory(context, Helper.getApplicationName(context))
        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
        return mediaSource.createMediaSource(uri)
    }

    private fun buildExtractorMediaSource(uri: Uri): ExtractorMediaSource {
        val dataSourceFactory = DefaultHttpDataSourceFactory(Helper.getApplicationName(context), bandwidthMeter)
        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
        return mediaSource.createMediaSource(uri)
    }

    inner class MediaViewHolder(val view: View) : RecyclerView.ViewHolder(view)


}