package com.mountsa.fm2027.model

data class Club(
    val id: String,
    val name: String,
    val shortName: String,
    val leagueId: String,
    val nationId: String,
    val stadiumId: String = "",
    val attackRating: Int = 0,
    val midfieldRating: Int = 0,
    val defenseRating: Int = 0,
    val overallRating: Int = 0,
    val transferBudget: Long = 0,
    val weeklyWage: Long = 0,
    val coach: String = "",
    val formation: String = "4-3-3"
)
