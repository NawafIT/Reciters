package com.api.reader.ui.mvvm

import com.api.reader.ui.api.WebAPI
import com.api.reader.ui.apiclass.Reciters

class Repository(private val WebAPI: WebAPI = WebAPI()) {
    suspend fun getReciters(): Reciters{
        return WebAPI.getReciters()
    }
}