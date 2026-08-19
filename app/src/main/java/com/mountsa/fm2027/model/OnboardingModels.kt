package com.mountsa.fm2027.model

data class ManagerProfile(
    val name: String = "",
    val age: Int = 25,
    val avatarRes: Int = 0,
    val avatarUri: String? = null
)

data class Country(
    val id: String,
    val name: String,
    val flagCode: String // ISO 3166-1 alpha-2 code for flags
) {
    val flagEmoji: String
        get() {
            if (flagCode.length != 2) return "🏳️"
            return try {
                val first = flagCode[0].uppercaseChar()
                val second = flagCode[1].uppercaseChar()
                
                val firstOffset = 0x1F1E6 + (first.code - 'A'.code)
                val secondOffset = 0x1F1E6 + (second.code - 'A'.code)
                
                String(Character.toChars(firstOffset)) + String(Character.toChars(secondOffset))
            } catch (e: Exception) {
                "🏳️"
            }
        }

    val flagUrl: String
        get() = "https://raw.githubusercontent.com/malithm/animated-country-flags/main/flags/$flagCode.gif"
}

data class Team(
    val id: String,
    val name: String,
    val shortName: String,
    val leagueId: String,
    val stadium: String = "",
    val overallRating: Int = 0,
    val attack: Int = 0,
    val midfield: Int = 0,
    val defense: Int = 0,
    val transferBudget: Long = 0,
    val weeklyWage: Long = 0,
    val coach: String = "",
    val formation: String = "4-3-3",
    val style: String = ""
)
