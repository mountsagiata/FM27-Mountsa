package com.mountsa.fm2027.viewmodel

import android.app.Application
import android.util.Log
import android.util.Xml
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mountsa.fm2027.model.Club
import com.mountsa.fm2027.model.ClubFinances
import com.mountsa.fm2027.model.League
import com.mountsa.fm2027.model.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

data class MatchEvent(
    val minute: Int,
    val type: String, // GOAL, YELLOW, RED
    val team: String, // HOME, AWAY
    val player: String,
    val assist: String? = null,
    val reason: String? = null
)

data class MatchScore(
    val home: Int = 0,
    val away: Int = 0
)

class ManagementViewModel(application: Application) : AndroidViewModel(application) {

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players.asStateFlow()

    private val _clubs = MutableStateFlow<List<Club>>(emptyList())
    val clubs: StateFlow<List<Club>> = _clubs.asStateFlow()

    private val _leagues = MutableStateFlow<List<League>>(emptyList())
    val leagues: StateFlow<List<League>> = _leagues.asStateFlow()

    private val _gold = MutableStateFlow(1250L)
    val gold: StateFlow<Long> = _gold.asStateFlow()

    private val _famousPoints = MutableStateFlow(840L)
    val famousPoints: StateFlow<Long> = _famousPoints.asStateFlow()

    private val _finances = MutableStateFlow(ClubFinances(
        budget = "€150,000,000",
        weeklyWage = "€2,500,000",
        sponsorName = "Global Sports Tech",
        seasonalBonus = "€10,000,000"
    ))
    val finances: StateFlow<ClubFinances> = _finances.asStateFlow()

    // Match Simulation State
    private val _isSimulating = MutableStateFlow(false)
    val isSimulating: StateFlow<Boolean> = _isSimulating.asStateFlow()

    private val _matchProgress = MutableStateFlow(0)
    val matchProgress: StateFlow<Int> = _matchProgress.asStateFlow()

    private val _score = MutableStateFlow(MatchScore())
    val score: StateFlow<MatchScore> = _score.asStateFlow()

    private val _matchEvents = MutableStateFlow<List<MatchEvent>>(emptyList())
    val matchEvents: StateFlow<List<MatchEvent>> = _matchEvents.asStateFlow()

    init {
        loadLeaguesAndClubs()
        loadPlayersFromXml()
    }

    private fun loadLeaguesAndClubs() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputStream: InputStream = getApplication<Application>().assets.open("data/teams/database_club.xml")
                val parser = Xml.newPullParser()
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                parser.setInput(inputStream, null)

                val clubList = mutableListOf<Club>()
                val leagueList = mutableListOf<League>()

                var eventType = parser.eventType
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        when (parser.name) {
                            "league" -> {
                                val id = parser.getAttributeValue(null, "id") ?: ""
                                val name = parser.getAttributeValue(null, "name") ?: ""
                                val nationId = parser.getAttributeValue(null, "nationId") ?: ""
                                leagueList.add(League(id, name, nationId))
                            }
                            "team" -> {
                                val id = parser.getAttributeValue(null, "id") ?: ""
                                val name = parser.getAttributeValue(null, "name") ?: ""
                                val shortName = parser.getAttributeValue(null, "short_name") ?: ""
                                val leagueId = parser.getAttributeValue(null, "leagueId") ?: ""
                                val nationId = parser.getAttributeValue(null, "nationId") ?: ""
                                
                                clubList.add(Club(id, name, shortName, leagueId, nationId))
                            }
                        }
                    }
                    eventType = parser.next()
                }
                _leagues.value = leagueList
                _clubs.value = clubList
            } catch (e: Exception) {
                Log.e("ManagementViewModel", "Error loading clubs", e)
            }
        }
    }

    private fun loadPlayersFromXml() {
        viewModelScope.launch(Dispatchers.IO) {
            val playerList = mutableListOf<Player>()
            try {
                val inputStream: InputStream = getApplication<Application>().assets.open("data/teams/database_players.xml")
                val parser = Xml.newPullParser()
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                parser.setInput(inputStream, null)

                var eventType = parser.eventType
                var currentPlayerId = ""
                var shortName = ""
                var position = ""
                var age = 0
                var nationality = ""
                var faceUrl = ""
                var value = ""
                var overall = 0
                var clubId = ""
                var clubName = ""
                
                var pace = 0
                var shooting = 0
                var passing = 0
                var dribbling = 0
                var defending = 0
                var physic = 0
                var skill = 0

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        when (parser.name) {
                            "player" -> {
                                currentPlayerId = parser.getAttributeValue(null, "id") ?: ""
                            }
                            "short_name" -> shortName = parser.nextText()
                            "positions" -> { /* Skip */ }
                            "position" -> if (position.isEmpty()) position = parser.nextText()
                            "overall" -> overall = parser.nextText().toIntOrNull() ?: 0
                            "value_eur" -> {
                                val rawValue = parser.nextText().toLongOrNull() ?: 0L
                                value = "€${rawValue / 1000000}M"
                            }
                            "age" -> age = parser.nextText().toIntOrNull() ?: 0
                            "nationality_name" -> nationality = parser.nextText()
                            "club_team_id" -> clubId = parser.nextText()
                            "club_name" -> clubName = parser.nextText()
                            "pace" -> pace = parser.nextText().toIntOrNull() ?: 0
                            "shooting" -> shooting = parser.nextText().toIntOrNull() ?: 0
                            "passing" -> passing = parser.nextText().toIntOrNull() ?: 0
                            "dribbling" -> dribbling = parser.nextText().toIntOrNull() ?: 0
                            "defending" -> defending = parser.nextText().toIntOrNull() ?: 0
                            "physic" -> physic = parser.nextText().toIntOrNull() ?: 0
                            "skill_moves" -> skill = parser.nextText().toIntOrNull() ?: 0
                            "player_face_url" -> faceUrl = parser.nextText()
                        }
                    } else if (eventType == XmlPullParser.END_TAG && parser.name == "player") {
                        if (shortName.isNotEmpty()) {
                            playerList.add(
                                Player(
                                    id = currentPlayerId,
                                    name = shortName,
                                    position = position,
                                    age = age,
                                    nationality = nationality,
                                    avatarUrl = faceUrl,
                                    rating = overall,
                                    value = value,
                                    clubId = clubId,
                                    clubName = clubName,
                                    pace = pace,
                                    shooting = shooting,
                                    passing = passing,
                                    dribbling = dribbling,
                                    defending = defending,
                                    physic = physic,
                                    skill = skill
                                )
                            )
                        }
                        // Reset fields
                        currentPlayerId = ""; shortName = ""; position = ""; age = 0; nationality = ""; faceUrl = ""; value = ""; overall = 0; clubId = ""; clubName = ""
                        pace = 0; shooting = 0; passing = 0; dribbling = 0; defending = 0; physic = 0; skill = 0
                    }
                    eventType = parser.next()
                }
                inputStream.close()
                _players.value = playerList
            } catch (e: Exception) {
                Log.e("ManagementViewModel", "Error loading players", e)
            }
        }
    }

    fun startMatchSimulation() {
        viewModelScope.launch {
            _isSimulating.value = true
            _matchProgress.value = 0
            _score.value = MatchScore()
            _matchEvents.value = emptyList()

            while (_matchProgress.value < 90) {
                delay(200)
                _matchProgress.value += 2

                if (Math.random() > 0.95) {
                    val rand = Math.random()
                    val type = if (rand > 0.4) "GOAL" else if (rand > 0.1) "YELLOW" else "RED"
                    val team = if (Math.random() > 0.5) "HOME" else "AWAY"
                    
                    var assist: String? = null
                    var reason: String? = null
                    
                    if (type == "GOAL") {
                        val currentScore = _score.value
                        _score.value = if (team == "HOME") currentScore.copy(home = currentScore.home + 1) else currentScore.copy(away = currentScore.away + 1)
                        assist = if (team == "HOME") "K. De Bruyne" else "A. Mac Allister"
                    } else {
                        val reasons = listOf("Tactical Foul", "Dissent", "Unsporting Conduct", "Dangerous Play", "Late Challenge")
                        reason = reasons.random()
                    }

                    val player = if (team == "HOME") (if (type == "GOAL") "E. Haaland" else "Rodri") else (_players.value.randomOrNull()?.name ?: "Unknown")
                    
                    val newEvent = MatchEvent(
                        minute = _matchProgress.value,
                        type = type,
                        team = team,
                        player = player,
                        assist = assist,
                        reason = reason
                    )
                    _matchEvents.value = listOf(newEvent) + _matchEvents.value
                }
            }
        }
    }

    fun confirmMatchResult() {
        _isSimulating.value = false
    }

    fun addFamous(points: Long) {
        _famousPoints.value += points
    }

    fun addGold(amount: Long) {
        _gold.value += amount
    }
}
