package com.mountsa.fm2027.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.CompareArrows
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mountsa.fm2027.viewmodel.ManagementViewModel

// FM2027 Design System Colors
val BgBlack = Color(0xFF000000)
val NeonGreen = Color(0xFF00FF88)
val GrayStats = Color(0xFF888888)
val PureWhite = Color(0xFFFFFFFF)
val BorderGrey = Color(0xFF222222)

// Statistik Colors
val ColorGood = Color(0xFF00C853)
val ColorMedBlue = Color(0xFF2979FF)
val ColorMedOrange = Color(0xFFFF9800)
val ColorBad = Color(0xFFD32F2F)

// Helper untuk border satu sisi (top/bottom/end), karena Modifier.border() bawaan
// hanya mendukung border di semua sisi sekaligus.
fun Modifier.borderBottom(width: androidx.compose.ui.unit.Dp, color: Color): Modifier = drawBehind {
    val strokeWidth = width.toPx()
    drawLine(color, Offset(0f, size.height - strokeWidth / 2), Offset(size.width, size.height - strokeWidth / 2), strokeWidth)
}

fun Modifier.borderTop(width: androidx.compose.ui.unit.Dp, color: Color): Modifier = drawBehind {
    val strokeWidth = width.toPx()
    drawLine(color, Offset(0f, strokeWidth / 2), Offset(size.width, strokeWidth / 2), strokeWidth)
}

fun Modifier.borderEnd(width: androidx.compose.ui.unit.Dp, color: Color): Modifier = drawBehind {
    val strokeWidth = width.toPx()
    drawLine(color, Offset(size.width - strokeWidth / 2, 0f), Offset(size.width - strokeWidth / 2, size.height), strokeWidth)
}

enum class CareerMenu {
    HOME, CLUB, TAKTIK, MATCH, TRANSFER, KONTRAK, PENGEMBANGAN, MEDIA, MEDICAL, SCOUTING, SHOP, JADWAL, STATISTIK, SETTINGS
}

@Composable
fun CareerModeScreen(
    viewModel: ManagementViewModel? = null,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit = {}
) {
    var activeMenu by remember { mutableStateOf(CareerMenu.HOME) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(BgBlack)
    ) {
        // SIDEBAR KIRI (250px)
        CareerSidebar(
            activeMenu = activeMenu,
            onMenuSelected = { activeMenu = it }
        )

        // AREA KONTEN UTAMA
        Column(modifier = Modifier.weight(1f)) {
            // TOPBAR
            CareerTopBar(
                managerName = "Alex Hunter",
                budget = "£145.2M",
                onBack = onBack
            )

            // MAIN CONTENT BOX
            Box(modifier = Modifier.weight(1f).padding(24.dp)) {
                when (activeMenu) {
                    CareerMenu.HOME -> CareerHomeDashboard()
                    CareerMenu.CLUB -> ClubMenuContent()
                    CareerMenu.TAKTIK -> TaktikContent()
                    CareerMenu.TRANSFER -> TransferContent()
                    CareerMenu.SHOP -> ShopContent()
                    CareerMenu.JADWAL -> JadwalContent()
                    CareerMenu.STATISTIK -> StatistikContent()
                    else -> PlaceholderContent(activeMenu.name)
                }
            }
            
            // RUNNING TEKS INFO BERLANGSUNG
            RunningInfoBanner()
        }
    }
}

@Composable
fun CareerSidebar(
    activeMenu: CareerMenu,
    onMenuSelected: (CareerMenu) -> Unit
) {
    Column(
        modifier = Modifier
            .width(250.dp)
            .fillMaxHeight()
            .background(BgBlack)
            .borderEnd(1.dp, BorderGrey)
            .verticalScroll(rememberScrollState())
            .padding(vertical = 24.dp)
    ) {
        // Logo FM27
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(NeonGreen, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("27", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text("FM2027", color = PureWhite, fontWeight = FontWeight.Black, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Navigation Items
        SidebarItem(CareerMenu.HOME, "Home", Icons.Default.Home, activeMenu == CareerMenu.HOME) { onMenuSelected(it) }
        SidebarItem(CareerMenu.CLUB, "Club", Icons.Default.Shield, activeMenu == CareerMenu.CLUB) { onMenuSelected(it) }
        SidebarItem(CareerMenu.MATCH, "Match", Icons.Default.SportsSoccer, activeMenu == CareerMenu.MATCH) { onMenuSelected(it) }
        SidebarItem(CareerMenu.TRANSFER, "Transfer", Icons.AutoMirrored.Rounded.CompareArrows, activeMenu == CareerMenu.TRANSFER) { onMenuSelected(it) }
        SidebarItem(CareerMenu.SHOP, "Shop", Icons.Default.ShoppingCart, activeMenu == CareerMenu.SHOP) { onMenuSelected(it) }
        SidebarItem(CareerMenu.JADWAL, "Jadwal", Icons.Default.CalendarMonth, activeMenu == CareerMenu.JADWAL) { onMenuSelected(it) }
        SidebarItem(CareerMenu.STATISTIK, "Statistik", Icons.Default.BarChart, activeMenu == CareerMenu.STATISTIK) { onMenuSelected(it) }
        
        Spacer(modifier = Modifier.height(16.dp))
        Divider(modifier = Modifier.padding(horizontal = 24.dp), color = BorderGrey)
        Spacer(modifier = Modifier.height(16.dp))

        SidebarItem(CareerMenu.TAKTIK, "Taktik", Icons.Default.Dashboard, activeMenu == CareerMenu.TAKTIK) { onMenuSelected(it) }
        SidebarItem(CareerMenu.KONTRAK, "Kontrak", Icons.Default.Handshake, activeMenu == CareerMenu.KONTRAK) { onMenuSelected(it) }
        SidebarItem(CareerMenu.PENGEMBANGAN, "Development", Icons.Default.TrendingUp, activeMenu == CareerMenu.PENGEMBANGAN) { onMenuSelected(it) }
        SidebarItem(CareerMenu.SCOUTING, "Scouting", Icons.Default.Search, activeMenu == CareerMenu.SCOUTING) { onMenuSelected(it) }
        SidebarItem(CareerMenu.MEDICAL, "Medical", Icons.Default.MedicalServices, activeMenu == CareerMenu.MEDICAL) { onMenuSelected(it) }
        SidebarItem(CareerMenu.MEDIA, "Media", Icons.Default.Mic, activeMenu == CareerMenu.MEDIA) { onMenuSelected(it) }

        Spacer(modifier = Modifier.weight(1f))
        
        SidebarItem(CareerMenu.SETTINGS, "Settings", Icons.Default.Settings, activeMenu == CareerMenu.SETTINGS) { onMenuSelected(it) }
    }
}

@Composable
fun SidebarItem(
    id: CareerMenu,
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: (CareerMenu) -> Unit
) {
    val contentColor = if (isSelected) NeonGreen else GrayStats

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) NeonGreen.copy(alpha = 0.1f) else Color.Transparent)
            .clickable { onClick(id) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            color = if (isSelected) PureWhite else GrayStats,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun CareerTopBar(
    managerName: String,
    budget: String,
    onBack: () -> Unit
) {
    Surface(
        color = BgBlack,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .borderBottom(1.dp, BorderGrey)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PureWhite)
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(managerName.uppercase(), color = NeonGreen, fontWeight = FontWeight.Black, fontSize = 14.sp)
                    Text("MANAGER • LIVERPOOL FC", color = PureWhite, fontSize = 10.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFF111111)).border(1.dp, NeonGreen, CircleShape)
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.padding(8.dp), tint = GrayStats)
                }
                Spacer(modifier = Modifier.width(24.dp))
                Box(modifier = Modifier.width(1.dp).height(30.dp).background(BorderGrey))
                Spacer(modifier = Modifier.width(24.dp))
                Column {
                    Text("TRANSFER BUDGET", color = GrayStats, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    Text(budget, color = PureWhite, fontSize = 16.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
fun CareerHomeDashboard() {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Text("DASHBOARD", color = PureWhite, fontWeight = FontWeight.Black, fontSize = 24.sp, letterSpacing = 2.sp)
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            DashboardStatCard(modifier = Modifier.weight(1.5f), title = "JADWAL BERIKUTNYA") {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxSize()) {
                    Text("LIV", color = PureWhite, fontWeight = FontWeight.Black, fontSize = 24.sp)
                    Text("VS", color = NeonGreen, fontWeight = FontWeight.Black, fontSize = 16.sp)
                    Text("ARS", color = PureWhite, fontWeight = FontWeight.Black, fontSize = 24.sp)
                }
            }
            
            DashboardStatCard(modifier = Modifier.weight(1f), title = "REKOR MUSIM") {
                Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
                    DashboardStatRow("WINS", "18", ColorGood)
                    DashboardStatRow("DRAWS", "4", PureWhite)
                    DashboardStatRow("LOSSES", "2", ColorBad)
                }
            }
            
            DashboardStatCard(modifier = Modifier.weight(1f), title = "PEMAIN TERBAIK") {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(Color(0xFF111111)).border(1.dp, NeonGreen, CircleShape))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("M. SALAH", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("24 GOALS", color = NeonGreen, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardStatCard(modifier: Modifier = Modifier, title: String, content: @Composable () -> Unit) {
    Surface(
        modifier = modifier.height(140.dp),
        color = Color(0xFF0A0A0A),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BorderGrey)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(title, color = GrayStats, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                content()
            }
        }
    }
}

@Composable
fun DashboardStatRow(label: String, value: String, color: Color) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = GrayStats, fontSize = 11.sp)
        Text(value, color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun RunningInfoBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .background(Color.Black.copy(alpha = 0.9f))
            .borderTop(1.dp, NeonGreen.copy(alpha = 0.2f)),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = "TRANSFER: AS Roma membeli striker dari Ajax senilai €30M • CEDERA: Kylian Mbappé absen 3 minggu • JADWAL PEKAN INI: Manchester Derby, Minggu malam • PERFORMA: Liverpool FC mencatatkan 10 kemenangan beruntun • ",
            color = NeonGreen,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp).basicMarquee(iterations = Int.MAX_VALUE)
        )
    }
}

@Composable
fun ClubMenuContent() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("SQUAD", "STAFF", "STADIUM", "FINANCE", "HISTORY")

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Text("CLUB MANAGEMENT", color = PureWhite, fontWeight = FontWeight.Black, fontSize = 24.sp)
        
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTab == index
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) NeonGreen else Color(0xFF111111))
                        .clickable { selectedTab = index }
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Text(title, color = if (isSelected) Color.Black else PureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (selectedTab) {
                0 -> SquadList()
                3 -> FinanceOverview()
                else -> PlaceholderContent(tabs[selectedTab])
            }
        }
    }
}

@Composable
fun SquadList() {
    val players = listOf(
        PlayerItem("Alisson Becker", "GK", 98, "Happy", 100),
        PlayerItem("V. van Dijk", "DF", 95, "Happy", 92),
        PlayerItem("A. Mac Allister", "MF", 88, "Normal", 85),
        PlayerItem("Mohamed Salah", "ST", 92, "Happy", 98),
        PlayerItem("Darwin Nunez", "ST", 75, "Normal", 80)
    )

    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(players) { player ->
            SquadPlayerRow(player)
        }
    }
}

data class PlayerItem(val name: String, val pos: String, val chem: Int, val mood: String, val stamina: Int)

@Composable
fun SquadPlayerRow(player: PlayerItem) {
    val posColor = when (player.pos) {
        "GK" -> ColorMedBlue
        "DF" -> ColorGood
        "MF" -> ColorMedOrange
        "ST" -> ColorBad
        else -> PureWhite
    }

    Surface(
        color = Color(0xFF0A0A0A),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(0.5.dp, BorderGrey)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFF1A1A1A)))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.width(150.dp)) {
                Text(player.name, color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Box(
                    modifier = Modifier.background(posColor.copy(alpha = 0.2f), RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(player.pos, color = posColor, fontSize = 10.sp, fontWeight = FontWeight.Black)
                }
            }
            
            Spacer(modifier = Modifier.width(24.dp))
            
            // CHEMISTRY
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("CHEM", color = GrayStats, fontSize = 8.sp)
                Text("${player.chem}", color = if(player.chem >= 70) ColorGood else ColorMedOrange, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            
            Spacer(modifier = Modifier.width(24.dp))
            
            // MOOD
            val moodEmoji = if(player.mood == "Happy") "😊" else "😐"
            Text(moodEmoji, fontSize = 16.sp)
            
            Spacer(modifier = Modifier.width(24.dp))
            
            // STAMINA
            Column(modifier = Modifier.weight(1f)) {
                Text("STAMINA ${player.stamina}%", color = GrayStats, fontSize = 8.sp)
                LinearProgressIndicator(
                    progress = { player.stamina / 100f },
                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                    color = if(player.stamina > 80) ColorGood else ColorMedOrange,
                    trackColor = Color(0xFF222222)
                )
            }
        }
    }
}

@Composable
fun FinanceOverview() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0A0A0A)),
        border = BorderStroke(1.dp, BorderGrey)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("FINANCIAL STATEMENT", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                FinanceCard(modifier = Modifier.weight(1f), "BUDGET TRANSFER", "£145,200,000", NeonGreen)
                FinanceCard(modifier = Modifier.weight(1f), "WAGE BUDGET", "£4.2M / week", PureWhite)
                FinanceCard(modifier = Modifier.weight(1f), "PROJECTED PROFIT", "+ £12.5M", ColorGood)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                modifier = Modifier.align(Alignment.End),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("NEGOSIASI SPONSOR", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun FinanceCard(modifier: Modifier, label: String, value: String, color: Color) {
    Column(modifier = modifier.background(Color(0xFF111111), RoundedCornerShape(8.dp)).padding(16.dp)) {
        Text(label, color = GrayStats, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        Text(value, color = color, fontSize = 14.sp, fontWeight = FontWeight.Black)
    }
}

// STUBS
@Composable fun TaktikContent() { Text("Taktik Editor stub", color = GrayStats) }
@Composable fun TransferContent() { Text("Transfer Market stub", color = GrayStats) }
@Composable fun ShopContent() { Text("Shopping stub", color = GrayStats) }
@Composable fun JadwalContent() { Text("Jadwal Calendar stub", color = GrayStats) }
@Composable fun StatistikContent() { Text("Detailed Statistics stub", color = GrayStats) }

@Composable
fun PlaceholderContent(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("SECTION $name IS UNDER CONSTRUCTION", color = GrayStats, fontWeight = FontWeight.Bold)
    }
}
