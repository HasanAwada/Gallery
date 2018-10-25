package com.zippyyum.media.model

/**
 * Created by Hasan.Awada on 10/24/2018.
 */
data class MediaItem(val id: Int, val name: String, val url: String, val type: MediaType, val descriptions: String?) {

    enum class MediaType {
        IMAGE, VIDEO, PDF
    }
}