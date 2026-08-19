package com.mountsa.fm2027.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mountsa.fm2027.ui.screens.*
import com.mountsa.fm2027.viewmodel.ManagementViewModel
import com.mountsa.fm2027.viewmodel.OnboardingViewModel

sealed class Screen(val route: String) {
    object Intro : Screen("intro")
    object Loading : Screen("loading")
    object ManagerCreation : Screen("manager_creation")
    object CountrySelection : Screen("country_selection")
    object LeagueSelection : Screen("league_selection")
    object TeamSelection : Screen("team_selection")
    object Home : Screen("home")
    object Career : Screen("career")
    object Single : Screen("single")
    object Transfer : Screen("transfer")
    object Sponsor : Screen("sponsor")
    object About : Screen("about")
    object Settings : Screen("settings")
}

@Composable
fun SetupNavGraph(navController: NavHostController) {
    val onboardingViewModel: OnboardingViewModel = viewModel()
    val managementViewModel: ManagementViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Intro.route
    ) {
        composable(route = Screen.Intro.route) {
            IntroScreen(onNext = {
                navController.navigate(Screen.Loading.route)
            })
        }
        composable(route = Screen.Loading.route) {
            LoadingScreen(onStart = {
                navController.navigate(Screen.ManagerCreation.route)
            })
        }
        composable(route = Screen.ManagerCreation.route) {
            ManagerProfileScreen(
                viewModel = onboardingViewModel,
                onNext = {
                    navController.navigate(Screen.CountrySelection.route)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = Screen.CountrySelection.route) {
            CountrySelectionScreen(
                viewModel = onboardingViewModel,
                onNext = {
                    navController.navigate(Screen.LeagueSelection.route)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = Screen.LeagueSelection.route) {
            LeagueSelectionScreen(
                viewModel = onboardingViewModel,
                onNext = {
                    navController.navigate(Screen.TeamSelection.route)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = Screen.TeamSelection.route) {
            TeamSelectionScreen(
                viewModel = onboardingViewModel,
                onFinish = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Intro.route) { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = Screen.Home.route) {
            HomeScreen(
                onboardingViewModel = onboardingViewModel,
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }
        composable(route = Screen.Career.route) {
            CareerModeScreen(
                viewModel = managementViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(route = Screen.Transfer.route) {
            TransferMarketScreen(
                viewModel = managementViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(route = Screen.Sponsor.route) {
            SponsorScreen(
                viewModel = managementViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(route = Screen.Single.route) {
            // Placeholder for Single Mode
            SingleModeScreen(onBack = { navController.popBackStack() })
        }
        composable(route = Screen.About.route) {
            AboutScreen(onBack = { navController.popBackStack() })
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
