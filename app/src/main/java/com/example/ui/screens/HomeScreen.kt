package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.MockData
import com.example.model.AppOverlay
import com.example.model.BottomTab
import com.example.ui.components.ExpandableFab
import com.example.ui.components.FabSubAction
import com.example.ui.components.GeometricBalanceBackground
import com.example.ui.components.GlassCard
import com.example.ui.components.LunaHeader
import com.example.ui.components.VoiceInputSheet
import com.example.ui.theme.*
import com.example.viewmodel.LunaViewModel

@Composable
fun HomeScreen(
    viewModel: LunaViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var isFabExpanded by remember { mutableStateOf(false) }
    var showVoiceSheet by remember { mutableStateOf(false) }

    val subActions = remember {
        listOf(
            FabSubAction(
                id = "chat",
                label = "Ask Luna AI",
                icon = Icons.Rounded.AutoAwesome,
                color = LunaPrimary,
                onClick = { viewModel.setBottomTab(BottomTab.CHAT) }
            ),
            FabSubAction(
                id = "voice",
                label = "Voice Assistant",
                icon = Icons.Rounded.Mic,
                color = LunaSecondary,
                onClick = { showVoiceSheet = true }
            ),
            FabSubAction(
                id = "journal",
                label = "Quick Reflection",
                icon = Icons.Rounded.EditNote,
                color = LunaAccent,
                onClick = { viewModel.setBottomTab(BottomTab.JOURNAL) }
            ),
            FabSubAction(
                id = "search",
                label = "Search & Tools",
                icon = Icons.Rounded.Search,
                color = LunaSuccess,
                onClick = { viewModel.openOverlay(AppOverlay.SEARCH) }
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Geometric Balance animated Canvas layer
        GeometricBalanceBackground(alphaMultiplier = 0.18f)

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LunaHeader(
                title = "Luna AI",
                subtitle = "Good Morning, ${state.userName.split(" ").first()} ✨",
                unreadNotificationsCount = state.notifications.count { !it.isRead },
                isDarkMode = state.isDarkMode,
                isOffline = state.isOfflineMode,
                onSearchClick = { viewModel.openOverlay(AppOverlay.SEARCH) },
                onNotificationClick = { viewModel.openOverlay(AppOverlay.NOTIFICATIONS) },
                onThemeToggle = { viewModel.toggleDarkMode(it) },
                onOfflineToggle = { viewModel.toggleOfflineMode() }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
            // Hero Banner
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                shape = RoundedCornerShape(28.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_luna_1785002455984),
                        contentDescription = "Hero Artwork",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(28.dp))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color.Black.copy(alpha = 0.65f),
                                        Color.Transparent
                                    )
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = LunaPrimary.copy(alpha = 0.85f)
                            ) {
                                Text(
                                    text = "Your Daily Sanctuary",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "How can Luna support\nyour journey today?",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                lineHeight = 26.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.White.copy(alpha = 0.25f))
                                    .clickable { viewModel.setBottomTab(BottomTab.CHAT) }
                                    .padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Start Conversation",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Mood Check-In Selector
            Text(
                text = "How are you feeling today?",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(MockData.moodOptions) { (emoji, label) ->
                    val isSelected = state.selectedMood == emoji
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { viewModel.setSelectedMood(emoji) },
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) LunaPrimary else MaterialTheme.colorScheme.surfaceVariant,
                        tonalElevation = if (isSelected) 4.dp else 0.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = emoji, fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = label,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Daily Quote Card
            GlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.FormatQuote,
                                contentDescription = null,
                                tint = LunaPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Daily Inspiration",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = LunaPrimary
                            )
                        }
                        Icon(
                            imageVector = Icons.Outlined.BookmarkBorder,
                            contentDescription = "Save Quote",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = MockData.dailyQuotes.first(),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Actions & AI Tools Grid
            Text(
                text = "Quick Assistant Tools",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickToolCard(
                    title = "Glow Skincare",
                    subtitle = "PM Routine",
                    icon = Icons.Outlined.Face,
                    color = LunaPrimary,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.sendMessage("Can you design a PM skincare routine for glowing skin?", "Beauty & Skincare")
                    }
                )
                QuickToolCard(
                    title = "Gentle Pilates",
                    subtitle = "15-min Flow",
                    icon = Icons.Outlined.FitnessCenter,
                    color = LunaSecondary,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.sendMessage("Guide me through a 15-minute gentle Pilates flow for posture", "Fitness")
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickToolCard(
                    title = "Career Boost",
                    subtitle = "Negotiation Tips",
                    icon = Icons.Outlined.WorkOutline,
                    color = LunaAccent,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.sendMessage("Give me salary negotiation confidence tips for my performance review", "Career")
                    }
                )
                QuickToolCard(
                    title = "Mindful Journal",
                    subtitle = "Daily Reflection",
                    icon = Icons.Outlined.EditNote,
                    color = LunaSuccess,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.setBottomTab(BottomTab.JOURNAL)
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Habit Ring Quick Status
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.setBottomTab(BottomTab.JOURNAL) }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(LunaPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "75%", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = LunaPrimary)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Daily Wellness Habits", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Text(text = "4 of 6 completed today • 12 day streak 🔥", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Featured AI Categories Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Explore AI Companions",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "See All",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LunaPrimary,
                    modifier = Modifier.clickable { viewModel.setBottomTab(BottomTab.EXPLORE) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(MockData.exploreCategories) { cat ->
                    GlassCard(
                        modifier = Modifier
                            .width(180.dp)
                            .height(140.dp),
                        onClick = {
                            viewModel.selectExploreCategory(cat)
                            viewModel.setBottomTab(BottomTab.EXPLORE)
                        }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(LunaPrimary.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.AutoAwesome,
                                        contentDescription = null,
                                        tint = LunaPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                if (cat.badge != null) {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = LunaSecondary.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = cat.badge,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = LunaSecondary,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            Column {
                                Text(
                                    text = cat.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = cat.subtitle,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    // Scrim overlay when FAB is expanded
    AnimatedVisibility(
        visible = isFabExpanded,
        enter = fadeIn(animationSpec = tween(200)),
        exit = fadeOut(animationSpec = tween(150))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.42f))
                .clickable { isFabExpanded = false }
        )
    }

    // Expanding Floating Action Button (FAB)
    ExpandableFab(
        subActions = subActions,
        isExpanded = isFabExpanded,
        onToggleExpand = { isFabExpanded = !isFabExpanded },
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(bottom = 20.dp, end = 20.dp)
    )

    // Voice Sheet overlay
    if (showVoiceSheet) {
        VoiceInputSheet(
            onDismiss = { showVoiceSheet = false },
            onSendVoicePrompt = { prompt ->
                showVoiceSheet = false
                viewModel.sendMessage(prompt)
                viewModel.setBottomTab(BottomTab.CHAT)
            }
        )
    }
}
}

@Composable
fun QuickToolCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
