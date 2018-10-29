package com.zippyyum.media.utilities


import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.CircularProgressDrawable
import android.util.Log
import android.widget.ImageView

import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.zippyyum.media.R
import okhttp3.ResponseBody
import java.io.*


object Helper {

    fun loadImage(context: Context, imageView: ImageView, imageURL: String) {
        try {

            Log.d("#####", "loadImage: ")
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

    fun getApplicationName(context: Context): String {
        val applicationInfo = context.applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(stringId)
    }

    fun downloadPDFToMemory(context: Context, body: ResponseBody, fileName: String): Boolean {
        try {
            // todo change the file location/name according to your needs
            val futureStudioIconFile = File(context?.getExternalFilesDir(null).toString() + File.separator + fileName + ".pdf")

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val fileReader = ByteArray(4096)
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(futureStudioIconFile)

                while (true) {
                    val read = inputStream?.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read ?: 0)
                    fileSizeDownloaded += read?.toLong() ?: 0
                }
                outputStream.flush()
                return true
            } catch (e: IOException) {
                return false
            } finally {
                if (inputStream != null) {
                    inputStream.close()
                }
                if (outputStream != null) {
                    outputStream.close()
                }
            }
        } catch (e: IOException) {
            return false
        }
    }

    fun downloadVideoToMemory(context: Context, url: String, fileName: String) {
        val uri = Uri.parse(url)
        // Create request for android download manager
        val request = DownloadManager.Request(uri)
        //Setting title of request
        request.setTitle("Data Download")
        //Setting description of request
        request.setDescription("Downloading video: $fileName ")
        //Set the local destination for the downloaded file to a path within the application's external files directory
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$fileName.mp4")

        context?.getSystemService(Context.DOWNLOAD_SERVICE)?.let { downloadManager ->
            //Enqueue download and save into referenceId
            (downloadManager as DownloadManager).enqueue(request)
        }
    }

    fun checkPermissions(activity: Activity): Boolean {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1)
            return false
        }
        return true
    }

}
