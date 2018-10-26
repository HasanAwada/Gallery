package com.zippyyum.media.utilities

import com.zippyyum.media.services.RetrofitClient
import com.zippyyum.media.services.RetrofitService

/**
 * Created by Hasan.Awada on 10/26/2018.
 */
object ApiUtils {
    val retrofitService: RetrofitService?
        get() = RetrofitClient.getClient().create(RetrofitService::class.java)
}
