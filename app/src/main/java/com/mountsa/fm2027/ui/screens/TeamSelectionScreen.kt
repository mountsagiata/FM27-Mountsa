package com.mountsa.fm2027.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mountsa.fm2027.viewmodel.OnboardingViewModel
import kotlinx.coroutines.launch

@Composable
fun TeamSelectionScreen(
    viewModel: OnboardingViewModel, 
    onFinish: () -> Unit,
    onBack: () -> Unit
) {
    val selectedCountry by viewModel.selectedCountry.collectAsStateWithLifecycle()
    val selectedLeague by viewModel.selectedLeague.collectAsStateWithLifecycle()
    val allTeams by viewModel.teams.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    
    val teams = remember(selectedLeague, allTeams) {
        selectedLeague?.let { league ->
            allTeams.filter { it.leagueId == league.id }
        } ?: emptyList()
    }

    if (isLoading && teams.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFF050B13)), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF2ECC71))
        }
        return
    }

    val pagerState = rememberPagerState(pageCount = { teams.size })
    val scope = rememberCoroutineScope()
    
    val fmGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF2ECC71), Color(0xFF27AE60))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050B13))
            .padding(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.align(Alignment.TopStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color(0xFF2ECC71), RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("FM", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 10.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("MANAGER SETUP 2027", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                Text("SEASON PROFILE INITIALIZATION", color = Color(0xFF2ECC71), fontSize = 7.sp, letterSpacing = 0.5.sp)
            }
        }

        // Main Content (Centered)
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Text(
                    text = "🏆",
                    fontSize = 70.sp
                )
                Text(
                    text = selectedCountry?.flagEmoji ?: "🏳️",
                    fontSize = 24.sp,
                    modifier = Modifier.offset(x = (-4).dp, y = (-4).dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = selectedLeague?.name?.uppercase() ?: "COMPETITION",
                color = Color(0xFF2ECC71),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Text(
                "SELECT TEAM",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.2.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (teams.isEmpty()) {
                Box(
                    modifier = Modifier
                        .width(320.dp)
                        .height(80.dp)
                        .background(Color(0xFF0A141D), RoundedCornerShape(8.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("NO TEAMS AVAILABLE", color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            } else {
                // Selection Pager
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .width(340.dp)
                        .background(Color(0xFF0A141D), RoundedCornerShape(8.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            if (pagerState.currentPage > 0) {
                                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                            }
                        },
                        enabled = pagerState.currentPage > 0,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Rounded.ChevronLeft, 
                            contentDescription = null, 
                            tint = if(pagerState.currentPage > 0) Color(0xFF2ECC71) else Color.Gray.copy(alpha = 0.3f),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.weight(1f).height(40.dp)
                    ) { page ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = teams[page].name.uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = teams[page].shortName,
                                color = Color(0xFF2ECC71),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            if (pagerState.currentPage < teams.size - 1) {
                                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                            }
                        },
                        enabled = pagerState.currentPage < teams.size - 1,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Rounded.ChevronRight, 
                            contentDescription = null, 
                            tint = if(pagerState.currentPage < teams.size - 1) Color(0xFF2ECC71) else Color.Gray.copy(alpha = 0.3f),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                
                // Team Rating info
                val currentTeam = teams[pagerState.currentPage]
                Spacer(modifier = Modifier.height(12.dp))
                
                Card(
                    modifier = Modifier
                        .width(320.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF0A141D).copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            SmallRatingItem("ATT", currentTeam.attack)
                            SmallRatingItem("MID", currentTeam.midfield)
                            SmallRatingItem("DEF", currentTeam.defense)
                            SmallRatingItem("OVR", currentTeam.overallRating)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${currentTeam.formation} • ${currentTeam.style}",
                            color = Color.Gray,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "${pagerState.currentPage + 1} / ${teams.size}",
                    color = Color.Gray,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Bottom Navigation
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            // Back Button (Bottom Left)
            Button(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .height(36.dp)
                    .width(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.05f)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = null, tint = Color.White.copy(alpha = 0.6f), modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(6.dp))
                Text("BACK", color = Color.White.copy(alpha = 0.6f), fontWeight = FontWeight.Bold, fontSize = 10.sp)
            }

            // Finish Button (Bottom Right)
            val isEnabled = teams.isNotEmpty()
            Button(
                onClick = {
                    if (isEnabled) {
                        viewModel.selectTeam(teams[pagerState.currentPage])
                        onFinish()
                    }
                },
                enabled = isEnabled,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .height(40.dp)
                    .width(180.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(if(isEnabled) fmGradient else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("CONFIRM & DEPLOY", color = if(isEnabled) Color.Black else Color.White.copy(0.3f), fontWeight = FontWeight.Bold, fontSize = 10.sp)
                        Spacer(Modifier.width(6.dp))
                        Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = if(isEnabled) Color.Black else Color.White.copy(0.3f), modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SmallRatingItem(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.Gray, fontSize = 8.sp, fontWeight = FontWeight.Bold)
        Text(value.toString(), color = Color(0xFF2ECC71), fontSize = 14.sp, fontWeight = FontWeight.Black)
    }
}
