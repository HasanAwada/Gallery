package com.zippyyum.media.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.util.FitPolicy

import com.zippyyum.media.R
import com.zippyyum.media.model.MediaItem
import com.zippyyum.media.utilities.ApiUtils
import com.zippyyum.media.utilities.Helper
import kotlinx.android.synthetic.main.fragment_pdfreader.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * Use the [PDFReaderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PDFReaderFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_pdfreader, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayPDF()
    }


    private fun displayPDF() {
        mediaItem?.let {
            val file = File(context?.getExternalFilesDir(null).toString() + File.separator + mediaItem!!.name + ".pdf")
            if (file.exists()) {
                pdfvMedia.fromFile(file).pageFitPolicy(FitPolicy.WIDTH)
                        .load()
            } else {
                val retrofitService = ApiUtils.retrofitService
                val call = retrofitService?.getPDFFile()

                call?.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        Log.d("", "server contacted and has file")

                        response.body()?.let { content -> Helper.downloadPDFToMemory(context!!, content, mediaItem!!.name) }

                        val file = File(activity?.getExternalFilesDir(null).toString() + File.separator + mediaItem!!.name + ".pdf")
                        file?.let{
                            if (file.exists()) {
                                pdfvMedia?.let {
                                    pdfvMedia.fromFile(file).pageFitPolicy(FitPolicy.WIDTH)
                                            .load()
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("", "error")
                    }
                })
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
         * @return A new instance of fragment PDFReaderFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(mediaItem: MediaItem): PDFReaderFragment {
            val fragment = PDFReaderFragment()
            val args = Bundle()
            args.putSerializable(MEDIA_ITEM, mediaItem)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(): PDFReaderFragment {
            return PDFReaderFragment()
        }
    }
}// Required empty public constructor
