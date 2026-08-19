package com.mountsa.fm2027.model

data class League(
    val id: String,
    val name: String,
    val countryId: String,
    val level: Int = 1
)
