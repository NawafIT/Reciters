package com.api.reader.ui.apiclass

data class Reciter(
    val id: Int,
    val letter: String,
    val moshaf: List<Moshaf>,
    val name: String
)