package com.zippyyum.media.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zippyyum.media.R
import com.zippyyum.media.model.MediaItem
import com.zippyyum.media.utilities.Helper
import kotlinx.android.synthetic.main.image_layout.view.*
import kotlinx.android.synthetic.main.pdf_layout.view.*
import kotlinx.android.synthetic.main.video_layout.view.*

/**
 * Created by Hasan.Awada on 10/25/2018.
 */
class MediaViewerAdapter(context: Context, mediaItems: ArrayList<MediaItem>) : Adapter<RecyclerView.ViewHolder>() {

    private var mMediaItems = mediaItems
    private val context = context

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
        when (mediaItem.type) {
            MediaItem.MediaType.IMAGE -> Helper.loadImage(context, view.itemView.ivMedia, mediaItem.url)
            MediaItem.MediaType.VIDEO -> view.itemView.videoView.text = mediaItem.name
            MediaItem.MediaType.PDF -> view.itemView.pdfView.text = mediaItem.name
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

    inner class MediaViewHolder(val view: View) : RecyclerView.ViewHolder(view)

}