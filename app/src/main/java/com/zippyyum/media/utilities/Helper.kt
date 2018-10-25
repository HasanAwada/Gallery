package com.zippyyum.media.utilities


import android.content.Context
import android.os.Build
import android.support.v4.widget.CircularProgressDrawable
import android.util.Log
import android.widget.ImageView

import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.zippyyum.media.R

object Helper {

    fun loadImage(context: Context, imageView: ImageView, imageURL: String) {
        try {

            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                circularProgressDrawable.setColorSchemeColors(context.resources.getColor(R.color.colorAccent, null))
            } else {
                circularProgressDrawable.setColorSchemeColors(context.resources.getColor(R.color.colorAccent))
            }
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            val options = RequestOptions()
                    .centerCrop()
                    .placeholder(circularProgressDrawable)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)

            GlideApp.with(context)
                    .load(imageURL)
                    .apply(options)
                    .into(imageView)

        } catch (e: Exception) {
            Log.e("", "loadImage: ", e)
        }

    }

}
