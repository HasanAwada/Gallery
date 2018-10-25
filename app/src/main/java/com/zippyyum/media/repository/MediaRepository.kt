package com.zippyyum.media.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zippyyum.media.R
import com.zippyyum.media.model.JsonModel
import com.zippyyum.media.model.MediaItem
import java.io.IOException
import java.io.InputStream


/**
 * Created by Hasan.Awada on 10/25/2018.
 */
class MediaRepository(private val context: Context) {

    fun getMediaModels(): ArrayList<MediaItem> {
        val jsonMedia = inputStreamToString(context.resources.openRawResource(R.raw.media_files))
        val listType = object : TypeToken<JsonModel<MediaItem>>() {}.type
        val jsonModel = Gson().fromJson<JsonModel<MediaItem>>(jsonMedia, listType)
        return jsonModel.payload
    }

    private fun inputStreamToString(inputStream: InputStream): String {
        return try {
            val bytes = ByteArray(inputStream.available())
            inputStream.read(bytes, 0, bytes.size)
            String(bytes)
        } catch (e: IOException) {
            ""
        }
    }
}