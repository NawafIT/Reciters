package com.api.reader.ui.api

import com.api.reader.ui.api.Constant.BASE_URL
import com.api.reader.ui.apiclass.Reciters
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class WebAPI {
    private var api:ApiReciter
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ApiReciter::class.java)
    }

    suspend fun getReciters(): Reciters {
       return api.getReciters()
    }
}