package com.zippyyum.media.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zippyyum.media.R
import com.zippyyum.media.model.MediaItem
import kotlinx.android.synthetic.main.media_file_name.view.*

/**
 * Created by Hasan.Awada on 10/24/2018.
 */
class MediaFilesNamesAdapter(private val context: Context, private val mediaItems: ArrayList<MediaItem>) : RecyclerView.Adapter<MediaFilesNamesAdapter.MenuItemNameViewHolder>() {

    private var mMediaItems: ArrayList<MediaItem> = ArrayList()

    init {
        this.mMediaItems = mediaItems
    }

    override fun getItemCount(): Int {
        return mediaItems.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MenuItemNameViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val view = inflater.inflate(R.layout.media_file_name, p0, false)

        return MenuItemNameViewHolder(view)
    }

    override fun onBindViewHolder(p0: MenuItemNameViewHolder, p1: Int) {
        mMediaItems.let {
            p0.itemView.tvMediaFileName.text = mMediaItems.get(p1)?.name
            if (p1 == mMediaItems.size.minus(1)) {
                p0.itemView.vSeparator.visibility = View.GONE
            }
        }
    }

    inner class MenuItemNameViewHolder(view: View) : RecyclerView.ViewHolder(view)

}

