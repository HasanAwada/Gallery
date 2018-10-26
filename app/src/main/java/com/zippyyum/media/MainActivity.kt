package com.zippyyum.media

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.zippyyum.media.adapter.MediaFilesNamesAdapter
import com.zippyyum.media.adapter.MediaViewerAdapter
import com.zippyyum.media.model.MediaItem
import com.zippyyum.media.repository.MediaRepository
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.widget.LinearSnapHelper
import android.util.Log
import android.view.View
import com.zippyyum.media.intefaces.ItemClickListener

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_toolbar.title = ""
        setSupportActionBar(main_toolbar)

        readMediaItemsNames()
    }

    private fun readMediaItemsNames() {
        initMediaItemsNames(MediaRepository(this).getMediaModels())
    }

    private fun initMediaItemsNames(mediaItems: ArrayList<MediaItem>) {
        mediaItems.let {

            val adapter = MediaFilesNamesAdapter(this, mediaItems)
            adapter.setItemClickListener(object : ItemClickListener{
                override fun onItemClick(view: View, position: Int){

                }
            })

            rvMediaItemsNames.adapter = adapter
            rvMediaItemsNames.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

            initMediaItems(mediaItems)
        }
    }

    private fun initMediaItems(mediaItems: ArrayList<MediaItem>) {
        rvMediaItems.adapter = MediaViewerAdapter(this, mediaItems!!)
        rvMediaItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rvMediaItems)
    }
}
