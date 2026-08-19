package com.mountsa.fm2027.model

data class Player(
    val id: String,
    val name: String,
    val position: String,
    val age: Int,
    val nationality: String,
    val avatarUrl: String,
    val rating: Int,
    val value: String,
    val clubId: String,
    val clubName: String,
    val pace: Int,
    val shooting: Int,
    val passing: Int,
    val dribbling: Int,
    val defending: Int,
    val physic: Int,
     val skill: Int,
    val energy: Int = 100,
    val form: String = "A",
    val status: String = "Active",
    val stamina: Int = 100,
    val mood: Int = 100
) {
    val speed: Int get() = pace
    val power: Int get() = physic
}
