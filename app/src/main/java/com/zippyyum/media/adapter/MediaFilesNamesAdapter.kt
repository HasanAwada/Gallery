package com.zippyyum.media.adapter

import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zippyyum.media.R
import com.zippyyum.media.model.MediaItem
import kotlinx.android.synthetic.main.media_file_name.view.*
import com.zippyyum.media.intefaces.ItemClickListener


/**
 * Created by Hasan.Awada on 10/24/2018.
 */
class MediaFilesNamesAdapter(private val context: Context, private val mediaItems: ArrayList<MediaItem>) : RecyclerView.Adapter<MediaFilesNamesAdapter.MenuItemNameViewHolder>() {

    private var mMediaItems: ArrayList<MediaItem> = ArrayList()
    private val mediaItemsNamesViews = ArrayList<View>()
    private var onItemClickListener: ItemClickListener? = null

    fun setItemClickListener(clickListener: ItemClickListener) {
        onItemClickListener = clickListener
    }

    init {
        this.mMediaItems = mediaItems
    }

    override fun getItemCount(): Int {
        return mediaItems.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MenuItemNameViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val view = inflater.inflate(R.layout.media_file_name, p0, false)

        mediaItemsNamesViews.add(view)

        return MenuItemNameViewHolder(view)
    }

    override fun onBindViewHolder(view: MenuItemNameViewHolder, position: Int) {
        mMediaItems.let {
            view.itemView.tvMediaFileName.text = mMediaItems[position]?.name
            if (position == mMediaItems.size.minus(1)) {
                view.itemView.vSeparator.visibility = View.GONE
            }
            if(position == 0){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mediaItemsNamesViews[position].tvMediaFileName.setTextColor(context.resources.getColor(R.color.orange, null))
                } else {
                    mediaItemsNamesViews[position].tvMediaFileName.setTextColor(context.resources.getColor(R.color.orange))
                }
            }
            view.itemView.setOnClickListener {
                onItemClickListener?.onItemClick(it, position)
                for (index: Int in 0 until mediaItemsNamesViews.size) {
                    if (index == position) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            mediaItemsNamesViews[index].tvMediaFileName.setTextColor(context.resources.getColor(R.color.orange, null))
                        } else {
                            mediaItemsNamesViews[index].tvMediaFileName.setTextColor(context.resources.getColor(R.color.orange))
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            mediaItemsNamesViews[index].tvMediaFileName.setTextColor(context.resources.getColor(R.color.colorAccent, null))
                        } else {
                            mediaItemsNamesViews[index].tvMediaFileName.setTextColor(context.resources.getColor(R.color.colorAccent))
                        }
                    }
                }
            }
        }
    }

    inner class MenuItemNameViewHolder(view: View) : RecyclerView.ViewHolder(view)

}

