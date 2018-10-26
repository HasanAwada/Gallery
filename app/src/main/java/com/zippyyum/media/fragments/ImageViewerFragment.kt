package com.zippyyum.media.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.zippyyum.media.R
import com.zippyyum.media.model.MediaItem
import com.zippyyum.media.utilities.Helper
import kotlinx.android.synthetic.main.fragment_image_viewer.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * Use the [ImageViewerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageViewerFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mediaItem: MediaItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mediaItem = arguments!!.getSerializable(MEDIA_ITEM) as MediaItem
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_viewer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { context ->
            mediaItem?.let { mediaItem ->
                Helper.loadImage(context, ivMedia, mediaItem.url)
            }
        }
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
         * @return A new instance of fragment ImageViewerFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(mediaItem: MediaItem): ImageViewerFragment {
            val fragment = ImageViewerFragment()
            val args = Bundle()
            args.putSerializable(MEDIA_ITEM, mediaItem)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(): ImageViewerFragment {
            return ImageViewerFragment()
        }
    }
}// Required empty public constructor
