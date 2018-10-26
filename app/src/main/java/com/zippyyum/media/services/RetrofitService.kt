package com.zippyyum.media.services

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by Pc on 10/26/2018.
 */
interface RetrofitService {

    @GET("/pdf-test.pdf")
    fun getPDFFile(): Call<ResponseBody>

}