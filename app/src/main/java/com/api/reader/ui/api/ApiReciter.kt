package com.api.reader.ui.api

import com.api.reader.ui.apiclass.Reciters
import retrofit2.http.GET

interface ApiReciter {
    @GET("reciters")
    suspend fun getReciters(): Reciters
}