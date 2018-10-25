package com.zippyyum.media.utilities;


import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;


public class Helper {

    public static void loadImage(final Context context, final ImageView imageView, String imageURL) {
        try {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
//                    .placeholder(R.drawable.loading)
//                    .error(R.drawable.images)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(500, 1000)
                    .priority(Priority.HIGH);

            Glide.with(context)
                    .load(imageURL)
                    .apply(options)
                    .into(imageView);

        } catch (Exception e) {
            Log.e("", "loadImage: ", e);
        }
    }

}
