package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.model.AppOverlay
import com.example.model.BottomTab
import com.example.ui.components.LunaBottomBar
import com.example.ui.screens.*
import com.example.ui.theme.LunaTheme
import com.example.viewmodel.LunaViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: LunaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val state by viewModel.uiState.collectAsState()

            LunaTheme(darkTheme = state.isDarkMode) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (state.currentOverlay == AppOverlay.NONE) {
                            LunaBottomBar(
                                selectedTab = state.currentTab,
                                onTabSelected = { viewModel.setBottomTab(it) }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        // Render Overlay Screens if active
                        when (state.currentOverlay) {
                            AppOverlay.SPLASH -> {
                                SplashScreen(
                                    onFinish = { viewModel.closeOverlay() }
                                )
                            }
                            AppOverlay.ONBOARDING -> {
                                OnboardingScreen(
                                    onGetStarted = { viewModel.closeOverlay() },
                                    onSkip = { viewModel.closeOverlay() }
                                )
                            }
                            AppOverlay.SETTINGS -> {
                                SettingsScreen(
                                    viewModel = viewModel,
                                    onBack = { viewModel.closeOverlay() }
                                )
                            }
                            AppOverlay.SUBSCRIPTION -> {
                                SubscriptionScreen(
                                    onBack = { viewModel.closeOverlay() }
                                )
                            }
                            AppOverlay.NOTIFICATIONS -> {
                                NotificationScreen(
                                    viewModel = viewModel,
                                    onBack = { viewModel.closeOverlay() }
                                )
                            }
                            AppOverlay.SEARCH -> {
                                SearchScreen(
                                    viewModel = viewModel,
                                    onBack = { viewModel.closeOverlay() }
                                )
                            }
                            AppOverlay.FAVORITES -> {
                                FavoritesScreen(
                                    viewModel = viewModel,
                                    onBack = { viewModel.closeOverlay() }
                                )
                            }
                            AppOverlay.HISTORY -> {
                                HistoryScreen(
                                    viewModel = viewModel,
                                    onBack = { viewModel.closeOverlay() }
                                )
                            }
                            AppOverlay.OFFLINE -> {
                                OfflineScreen(
                                    onReconnect = { viewModel.toggleOfflineMode() }
                                )
                            }
                            AppOverlay.ABOUT -> {
                                AboutScreen(
                                    onBack = { viewModel.closeOverlay() }
                                )
                            }
                            AppOverlay.NONE, AppOverlay.WELCOME, AppOverlay.NOT_FOUND -> {
                                // Render Primary Tabs
                                Crossfade(
                                    targetState = state.currentTab,
                                    label = "tabTransition"
                                ) { tab ->
                                    when (tab) {
                                        BottomTab.HOME -> HomeScreen(viewModel = viewModel)
                                        BottomTab.CHAT -> ChatScreen(viewModel = viewModel)
                                        BottomTab.EXPLORE -> ExploreScreen(viewModel = viewModel)
                                        BottomTab.JOURNAL -> JournalScreen(viewModel = viewModel)
                                        BottomTab.PROFILE -> ProfileScreen(viewModel = viewModel)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
