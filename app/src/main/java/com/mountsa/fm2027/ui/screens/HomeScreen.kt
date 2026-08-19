package com.mountsa.fm2027.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
 import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mountsa.fm2027.viewmodel.OnboardingViewModel

@Composable
fun HomeScreen(
    onboardingViewModel: OnboardingViewModel,
    onNavigate: (String) -> Unit
) {
    val profile by onboardingViewModel.profile.collectAsState()
    val selectedTeam by onboardingViewModel.selectedTeam.collectAsState()
    val selectedCountry by onboardingViewModel.selectedCountry.collectAsState()
    val selectedLeague by onboardingViewModel.selectedLeague.collectAsState()
    
    var selectedModuleIndex by remember { mutableIntStateOf(1) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Navigation Bar
            HomeTopBar(
                managerName = profile.name.ifEmpty { "MANAGER" }, 
                clubName = selectedTeam?.name?.ifEmpty { "NO CLUB" } ?: "NO CLUB",
                budget = selectedTeam?.transferBudget ?: 500000000,
                onShopClick = { onNavigate("shop") },
                onSettingsClick = { onNavigate("settings") }
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Main Cards Grid
                Column(
                    modifier = Modifier.weight(3f),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        HomeModuleCard(
                            modifier = Modifier.weight(1f),
                            title = "CAREER MODE",
                            items = listOf("MATCH DAY", "STANDING", "MY CLUB", "TRANSFERS", "START SEASON"),
                            isSelected = selectedModuleIndex == 1,
                            onClick = { selectedModuleIndex = 1 },
                            onEnter = { onNavigate("career") }
                        )
                        HomeModuleCard(
                            modifier = Modifier.weight(1f),
                            title = "SINGLE MODE",
                            items = listOf("QUICK MATCH"),
                            isSelected = selectedModuleIndex == 2,
                            onClick = { selectedModuleIndex = 2 },
                            onEnter = { onNavigate("single") }
                        )
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        HomeModuleCard(
                            modifier = Modifier.weight(1f),
                            title = "TRANSFER MARKET",
                            items = listOf("GLOBAL SEARCH", "PLAYER DATABASE"),
                            isSelected = selectedModuleIndex == 3,
                            onClick = { selectedModuleIndex = 3 },
                            onEnter = { onNavigate("transfer") }
                        )
                        HomeModuleCard(
                            modifier = Modifier.weight(1f),
                            title = "SPONSORSHIP",
                            items = listOf("BUDGET GAIN", "FAMOUS POINTS"),
                            isSelected = selectedModuleIndex == 4,
                            onClick = { selectedModuleIndex = 4 },
                            onEnter = { onNavigate("sponsor") }
                        )
                    }
                }

                // Right Sidebar
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SetupSummaryPanel(
                        countryName = selectedCountry?.name ?: "NOT SELECTED",
                        countryFlag = selectedCountry?.flagEmoji ?: "🏳️",
                        league = selectedLeague?.name ?: "NOT SELECTED",
                        club = selectedTeam?.name ?: "NOT SELECTED"
                    )
                    
                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = { onNavigate("career") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("START SEASON", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun HomeTopBar(
    managerName: String, 
    clubName: String, 
    budget: Long, 
    onShopClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().background(Color.Black)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFF2ECC71), RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("FM", color = Color(0xFF090909), fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("FOOTBALL MANAGER 2027", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("LIVE STATUS: ACTIVE", color = Color(0xFF2ECC71), fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
            }

            // Manager Info Section
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF131313))
                        .border(1.dp, Color.LightGray, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Rounded.AccountCircle, 
                        contentDescription = null, 
                        tint = Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(managerName.uppercase(), color = Color(0xFF2ECC71), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Text(clubName.uppercase(), color = Color.White, fontSize = 9.sp)
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column(horizontalAlignment = Alignment.End) {
                    Text("BUDGET", color = Color.Gray, fontSize = 8.sp)
                    Text("$ ${budget / 1000000} M", color = Color(0xFF2ECC71), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.width(20.dp))
                // Shopping Cart Icon (Replaced Notification)
                IconButton(onClick = onShopClick) {
                    Icon(Icons.Rounded.ShoppingCart, contentDescription = "Shop", tint = Color.White, modifier = Modifier.size(22.dp))
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(Icons.Rounded.Settings, contentDescription = "Settings", tint = Color.White, modifier = Modifier.size(22.dp))
                }
            }
        }

        // Running Text Animated Feed (Every 5 seconds effect via marquee iterations)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2ECC71).copy(alpha = 0.6f))
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = "REAL LIFE FOOTBALL NEWS: Kylian Mbappé officially joins Real Madrid on a five-year deal • Manchester City win fourth consecutive Premier League title • Rodri wins the 2024 Ballon d'Or • Ruben Amorim starts at Man Utd • ",
                color = Color(0xFF090909),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                modifier = Modifier.basicMarquee(
                    iterations = Int.MAX_VALUE,
                    velocity = 50.dp
                )
            )
        }
    }
}
@Composable
fun HomeModuleCard(
    modifier: Modifier = Modifier,
    title: String,
    items: List<String>,
    isSelected: Boolean,
    onClick: () -> Unit,
    onEnter: () -> Unit
) {
    // Gunakan key untuk memaksa recomposition saat isSelected berubah
    key(isSelected) {
        Card(
            modifier = modifier
                .drawWithContent {
                    drawContent()
                    if (isSelected) {
                        drawRoundRect(
                            color = Color(0xFF2ECC71),
                            style = Stroke(width = 2.dp.toPx()),
                            cornerRadius = CornerRadius(12.dp.toPx())
                        )
                    }
                }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                ),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                Column {
                    Text(
                        text = title,
                        color = if (isSelected) Color(0xFF2ECC71) else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    items.forEach { item ->
                        Text(
                            text = "▸ $item",
                            color = Color.White,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(vertical = 2.dp),
                            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                        )
                    }
                }

                if (isSelected) {
                    Button(
                        onClick = onEnter,
                        modifier = Modifier.align(Alignment.BottomEnd).height(32.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2ECC71),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            "ENTER",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SetupSummaryPanel(countryName: String, countryFlag: String, league: String, club: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Black),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("SETUP SUMMARY", color = Color(0xFF2ECC71), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            SummaryItem("NATION", countryName, countryFlag)
            SummaryItem("COMPETITION", league)
            SummaryItem("CLUB", club)
        }
    }
}

@Composable
fun SummaryItem(label: String, value: String, icon: String? = null) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(label, color = Color.LightGray, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Text(icon, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(value.uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}