package com.androidcamp.liveflix.models

data class Channel(
    val countries: List<Country>,
    val languages: List<Language>,
    val logo: String,
    val name: String,
    val url: String
)