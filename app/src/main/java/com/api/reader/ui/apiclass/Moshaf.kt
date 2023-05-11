package com.api.reader.ui.apiclass

data class Moshaf(
    val id: Int,
    val name: String,
    val server: String,
    val surah_list: String,
    val surah_total: Int
)