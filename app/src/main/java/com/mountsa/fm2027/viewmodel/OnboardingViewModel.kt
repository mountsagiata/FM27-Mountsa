package com.mountsa.fm2027.viewmodel

import android.app.Application
import android.util.Log
import android.util.Xml
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mountsa.fm2027.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

class OnboardingViewModel(application: Application) : AndroidViewModel(application) {
    private val _profile = MutableStateFlow(ManagerProfile())
    val profile: StateFlow<ManagerProfile> = _profile.asStateFlow()
    
    private val _selectedCountry = MutableStateFlow<Country?>(null)
    val selectedCountry: StateFlow<Country?> = _selectedCountry.asStateFlow()
    
    private val _selectedLeague = MutableStateFlow<League?>(null)
    val selectedLeague: StateFlow<League?> = _selectedLeague.asStateFlow()
    
    private val _selectedTeam = MutableStateFlow<Team?>(null)
    val selectedTeam: StateFlow<Team?> = _selectedTeam.asStateFlow()
    
    // Country data is now dynamic and loaded from the XML database
    private val _countries = MutableStateFlow<List<Country>>(emptyList())
    val countries: StateFlow<List<Country>> = _countries.asStateFlow()

    private val _leagues = MutableStateFlow<List<League>>(emptyList())
    val leagues: StateFlow<List<League>> = _leagues.asStateFlow()
    
    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> = _teams.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                loadLocalDatabase()
            } catch (e: Exception) {
                Log.e("OnboardingViewModel", "Error loading data", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun getFlagCode(name: String): String {
        return when (name.lowercase()) {
            "england" -> "GB"
            "germany" -> "DE"
            "italy" -> "IT"
            "france" -> "FR"
            "spain" -> "ES"
            "netherlands" -> "NL"
            "portugal" -> "PT"
            "scotland" -> "GB"
            "turkey" -> "TR"
            "norway" -> "NO"
            "sweden" -> "SE"
            "brazil" -> "BR"
            "argentina" -> "AR"
            "united states" -> "US"
            "canada" -> "CA"
            "mexico" -> "MX"
            "indonesia" -> "ID"
            "china pr" -> "CN"
            "japan" -> "JP"
            "korea republic" -> "KR"
            "saudi arabia" -> "SA"
            "india" -> "IN"
            "denmark" -> "DK"
            "belgium" -> "BE"
            "croatia" -> "HR"
            "czech republic" -> "CZ"
            "poland" -> "PL"
            "romania" -> "RO"
            "switzerland" -> "CH"
            "austria" -> "AT"
            "greece" -> "GR"
            "republic of ireland" -> "IE"
            "egypt" -> "EG"
            "morocco" -> "MA"
            "nigeria" -> "NG"
            "ghana" -> "GH"
            "cameroon" -> "CM"
            "senegal" -> "SN"
            "south africa" -> "ZA"
            else -> if (name.length >= 2) name.substring(0, 2).uppercase() else "UN"
        }
    }

    private fun loadLocalDatabase() {
        val localCountries = mutableListOf<Country>()
        val localLeagues = mutableListOf<League>()
        val localTeams = mutableListOf<Team>()
        
        try {
            val inputStream: InputStream = getApplication<Application>().assets.open("data/teams/database_club.xml")
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            
            var eventType = parser.eventType
            var currentLeagueId = ""
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    val tagName = parser.name
                    when (tagName) {
                        "nation" -> {
                            val id = parser.getAttributeValue(null, "id") ?: ""
                            val name = parser.getAttributeValue(null, "name") ?: ""
                            if (id.isNotEmpty() && name.isNotEmpty()) {
                                if (localCountries.none { it.id == id }) {
                                    localCountries.add(Country(id, name, getFlagCode(name)))
                                }
                            }
                        }
                        "league" -> {
                            val id = parser.getAttributeValue(null, "id") ?: ""
                            val name = parser.getAttributeValue(null, "name") ?: ""
                            
                            val nationIdAttr = parser.getAttributeValue(null, "nationId")
                            val countryAttr = parser.getAttributeValue(null, "country")
                            
                            val nationId = if (nationIdAttr != null) {
                                nationIdAttr
                            } else if (countryAttr != null) {
                                localCountries.find { it.name.equals(countryAttr, ignoreCase = true) }?.id ?: countryAttr.lowercase()
                            } else {
                                ""
                            }

                            val level = parser.getAttributeValue(null, "level")?.toIntOrNull() ?: 1
                            
                            if (id.isNotEmpty()) {
                                if (localLeagues.none { it.id == id }) {
                                    localLeagues.add(League(id, name, nationId, level))
                                }
                                currentLeagueId = id
                            }
                        }
                        "team" -> {
                            val id = parser.getAttributeValue(null, "id") ?: ""
                            val name = parser.getAttributeValue(null, "name") ?: ""
                            val shortName = parser.getAttributeValue(null, "short_name") ?: ""
                            val stadium = parser.getAttributeValue(null, "stadium") ?: ""
                            val teamLeagueId = parser.getAttributeValue(null, "leagueId") ?: currentLeagueId
                            
                            var attack = 0
                            var midfield = 0
                            var defense = 0
                            var overall = 0
                            var transferBudget = 0L
                            var weeklyWage = 0L
                            var coach = ""
                            var formation = "4-3-3"
                            var style = ""
                            
                            var depth = 1
                            while (depth > 0) {
                                val nextType = parser.next()
                                if (nextType == XmlPullParser.START_TAG) {
                                    depth++
                                    when (parser.name) {
                                        "ratings" -> {
                                            attack = parser.getAttributeValue(null, "attack")?.toIntOrNull() ?: 0
                                            midfield = parser.getAttributeValue(null, "midfield")?.toIntOrNull() ?: 0
                                            defense = parser.getAttributeValue(null, "defense")?.toIntOrNull() ?: 0
                                            overall = parser.getAttributeValue(null, "overall")?.toIntOrNull() ?: 0
                                        }
                                        "financial" -> {
                                            transferBudget = parser.getAttributeValue(null, "transfer_budget")?.toLongOrNull() ?: 0L
                                            weeklyWage = parser.getAttributeValue(null, "weekly_wage")?.toLongOrNull() ?: 0L
                                        }
                                        "tactics" -> {
                                            formation = parser.getAttributeValue(null, "formation") ?: "4-3-3"
                                            coach = parser.getAttributeValue(null, "coach") ?: ""
                                            style = parser.getAttributeValue(null, "style") ?: ""
                                        }
                                    }
                                } else if (nextType == XmlPullParser.END_TAG) {
                                    depth--
                                }
                            }
                            
                            localTeams.add(Team(
                                id, name, shortName, teamLeagueId, stadium,
                                overall, attack, midfield, defense, transferBudget, weeklyWage,
                                coach, formation, style
                            ))
                        }
                    }
                }
                eventType = parser.next()
            }
            inputStream.close()
            
            _countries.value = localCountries
            _leagues.value = localLeagues
            _teams.value = localTeams
            
        } catch (e: Exception) {
            Log.e("OnboardingViewModel", "Error loading local database", e)
        }
    }
    
    fun updateProfile(name: String, age: Int, avatarRes: Int, avatarUri: String? = null) {
        _profile.value = ManagerProfile(name, age, avatarRes, avatarUri)
    }
    
    fun selectCountry(country: Country) {
        _selectedCountry.value = country
        _selectedLeague.value = null
        _selectedTeam.value = null
    }
    
    fun selectLeague(league: League) {
        _selectedLeague.value = league
        _selectedTeam.value = null
    }
    
    fun selectTeam(team: Team) {
        _selectedTeam.value = team
    }
    
    fun getLeaguesForCountry(countryId: String): List<League> {
        return _leagues.value.filter { it.countryId == countryId }
    }
    
    fun getTeamsForLeague(leagueId: String): List<Team> {
        return _teams.value.filter { it.leagueId == leagueId }
    }
}
