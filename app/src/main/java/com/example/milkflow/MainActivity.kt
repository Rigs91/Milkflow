package com.example.milkflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.milkflow.data.SettingsRepository
import com.example.milkflow.data.local.MilkFlowDatabase
import com.example.milkflow.data.repository.MilkFlowRepository
import com.example.milkflow.MilkFlowViewModelFactory
import com.example.milkflow.ui.navigation.Routes
import com.example.milkflow.ui.screen.bottle.BottleLogScreen
import com.example.milkflow.ui.screen.bottle.BottleLogViewModel
import com.example.milkflow.ui.screen.history.HistoryScreen
import com.example.milkflow.ui.screen.history.HistoryViewModel
import com.example.milkflow.ui.screen.home.HomeScreen
import com.example.milkflow.ui.screen.home.HomeViewModel
import com.example.milkflow.ui.screen.nursing.NursingSessionScreen
import com.example.milkflow.ui.screen.nursing.NursingSessionViewModel
import com.example.milkflow.ui.screen.pump.PumpLogScreen
import com.example.milkflow.ui.screen.pump.PumpLogViewModel
import com.example.milkflow.ui.screen.settings.SettingsScreen
import com.example.milkflow.ui.screen.settings.SettingsViewModel
import com.example.milkflow.ui.theme.MilkFlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = MilkFlowDatabase.getDatabase(applicationContext)
        val repository = MilkFlowRepository(database.babyProfileDao(), database.feedLogDao())
        val settingsRepository = SettingsRepository(applicationContext)
        val factory = MilkFlowViewModelFactory(repository, settingsRepository)

        setContent {
            MilkFlowTheme {
                val navController = rememberNavController()
                val activeBaby by repository.activeBaby.collectAsState(initial = null)
                val settings by settingsRepository.settings.collectAsState(initial = null)

                val homeViewModel: HomeViewModel = viewModel(factory = factory)

                NavHost(navController = navController, startDestination = Routes.HOME) {
                    composable(Routes.HOME) {
                        HomeScreen(
                            viewModel = homeViewModel,
                            onStartNursing = { navController.navigate(Routes.NURSING) },
                            onLogBottle = { navController.navigate(Routes.BOTTLE) },
                            onLogPump = { navController.navigate(Routes.PUMP) },
                            onHistory = { navController.navigate(Routes.HISTORY) },
                            onSettings = { navController.navigate(Routes.SETTINGS) }
                        )
                    }
                    composable(Routes.NURSING) {
                        val vm: NursingSessionViewModel = viewModel(factory = factory)
                        activeBaby?.let { vm.setBaby(it.id) }
                        NursingSessionScreen(viewModel = vm) {
                            navController.popBackStack()
                        }
                    }
                    composable(Routes.BOTTLE) {
                        val vm: BottleLogViewModel = viewModel(factory = factory)
                        activeBaby?.let { vm.setBaby(it.id) }
                        BottleLogScreen(
                            viewModel = vm,
                            is24Hour = settings?.is24Hour ?: true,
                            onSaved = { navController.popBackStack() },
                            onCancel = { navController.popBackStack() }
                        )
                    }
                    composable(Routes.PUMP) {
                        val vm: PumpLogViewModel = viewModel(factory = factory)
                        activeBaby?.let { vm.setBaby(it.id) }
                        PumpLogScreen(
                            viewModel = vm,
                            is24Hour = settings?.is24Hour ?: true,
                            onSaved = { navController.popBackStack() },
                            onCancel = { navController.popBackStack() }
                        )
                    }
                    composable(Routes.HISTORY) {
                        val vm: HistoryViewModel = viewModel(factory = factory)
                        HistoryScreen(viewModel = vm) { navController.popBackStack() }
                    }
                    composable(Routes.SETTINGS) {
                        val vm: SettingsViewModel = viewModel(factory = factory)
                        SettingsScreen(viewModel = vm) { navController.popBackStack() }
                    }
                }
            }
        }
    }
}
