package com.mountsa.fm2027

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mountsa.fm2027.ui.screens.*
import com.mountsa.fm2027.ui.theme.Fm2027Theme
import com.mountsa.fm2027.viewmodel.ManagementViewModel
import com.mountsa.fm2027.viewmodel.OnboardingViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT)
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        hideSystemBars()
        
        setContent {
            Fm2027Theme(darkTheme = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    FM2027App()
                }
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemBars()
        }
    }

    private fun hideSystemBars() {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }
}

@Composable
fun FM2027App() {
    val navController = rememberNavController()
    val onboardingViewModel: OnboardingViewModel = viewModel()
    val managementViewModel: ManagementViewModel = viewModel()
    
    NavHost(
        navController = navController,
        startDestination = "intro"
    ) {
        composable("intro") {
            IntroScreen(onNext = {
                navController.navigate("manager_profile") {
                    popUpTo("intro") { inclusive = true }
                }
            })
        }
        
        composable("manager_profile") {
            ManagerProfileScreen(
                viewModel = onboardingViewModel,
                onNext = {
                    navController.navigate("country_selection")
                },
                onBack = {
                    navController.navigateUp()
                }
            )
        }
        
        composable("country_selection") {
            CountrySelectionScreen(
                viewModel = onboardingViewModel,
                onNext = {
                    navController.navigate("league_selection")
                },
                onBack = {
                    navController.navigateUp()
                }
            )
        }
        
        composable("league_selection") {
            LeagueSelectionScreen(
                viewModel = onboardingViewModel,
                onNext = {
                    navController.navigate("team_selection")
                },
                onBack = {
                    navController.navigateUp()
                }
            )
        }
        
        composable("team_selection") {
            TeamSelectionScreen(
                viewModel = onboardingViewModel,
                onFinish = {
                    navController.navigate("home") {
                        popUpTo("intro") { inclusive = true }
                    }
                },
                onBack = {
                    navController.navigateUp()
                }
            )
        }
        
        composable("home") {
            HomeScreen(
                onboardingViewModel = onboardingViewModel,
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable("career") { 
            CareerModeScreen(
                viewModel = managementViewModel, 
                onBack = { navController.navigateUp() }
            ) 
        }
        
        composable("single") { 
            SingleModeScreen(
                onBack = { navController.navigateUp() }
            ) 
        }
        
        composable("transfer") { 
            TransferMarketScreen(
                viewModel = managementViewModel,
                onBack = { navController.navigateUp() }
            ) 
        }
        
        composable("sponsor") { 
            SponsorScreen(
                viewModel = managementViewModel,
                onBack = { navController.navigateUp() }
            ) 
        }
        
        composable("settings") { 
            SettingsScreen(
                onBack = { navController.navigateUp() }
            ) 
        }
        
        composable("shop") { 
            ShopScreen(
                onBack = { navController.navigateUp() }
            ) 
        }
    }
}
