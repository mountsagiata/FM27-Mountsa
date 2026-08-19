package com.mountsa.fm2027.model

data class ClubFinances(
    val budget: String,
    val weeklyWage: String,
    val sponsorName: String,
    val seasonalBonus: String,
    val gold: Long = 1000,
    val famousPoints: Long = 500
)
