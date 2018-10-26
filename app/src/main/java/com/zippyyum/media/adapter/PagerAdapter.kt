package com.zippyyum.media.adapter

import android.media.browse.MediaBrowser
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.zippyyum.media.fragments.ImageViewerFragment
import com.zippyyum.media.fragments.PDFReaderFragment
import com.zippyyum.media.fragments.VideoPlayerFragment
import com.zippyyum.media.model.MediaItem

/**
 * Created by Hasan.Awada on 10/26/2018.
 */
class PagerAdapter(fm: FragmentManager, private var mediaItems: ArrayList<MediaItem>) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (mediaItems[position].type) {
            MediaItem.MediaType.IMAGE -> ImageViewerFragment.newInstance(mediaItems[position])
            MediaItem.MediaType.VIDEO -> VideoPlayerFragment.newInstance(mediaItems[position])
            MediaItem.MediaType.PDF -> PDFReaderFragment.newInstance(mediaItems[position])
        }
    }

    override fun getCount(): Int {
        return mediaItems.size
    }

}