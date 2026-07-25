package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BottomTab
import com.example.ui.theme.LunaPrimary
import com.example.ui.theme.LunaSecondary

data class NavTabItem(
    val tab: BottomTab,
    val label: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector
)

@Composable
fun LunaBottomBar(
    selectedTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavTabItem(BottomTab.HOME, "Home", Icons.Rounded.Home, Icons.Outlined.Home),
        NavTabItem(BottomTab.CHAT, "Chat", Icons.Rounded.AutoAwesome, Icons.Outlined.AutoAwesome),
        NavTabItem(BottomTab.EXPLORE, "Explore", Icons.Rounded.Explore, Icons.Outlined.Explore),
        NavTabItem(BottomTab.JOURNAL, "Journal", Icons.Rounded.EditNote, Icons.Outlined.EditNote),
        NavTabItem(BottomTab.PROFILE, "Profile", Icons.Rounded.Person, Icons.Outlined.Person)
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        tonalElevation = 8.dp,
        shadowElevation = 16.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = item.tab == selectedTab

                val animatedScale by animateFloatAsState(
                    targetValue = if (isSelected) 1.08f else 1.0f,
                    animationSpec = spring(stiffness = 300f),
                    label = "tabScale"
                )

                val activeBgColor by animateColorAsState(
                    targetValue = if (isSelected) LunaPrimary.copy(alpha = 0.12f) else Color.Transparent,
                    label = "tabBgColor"
                )

                val activeTextColor by animateColorAsState(
                    targetValue = if (isSelected) LunaPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    label = "tabTextColor"
                )

                Box(
                    modifier = Modifier
                        .scale(animatedScale)
                        .clip(RoundedCornerShape(20.dp))
                        .background(activeBgColor)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onTabSelected(item.tab) }
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) item.activeIcon else item.inactiveIcon,
                            contentDescription = item.label,
                            tint = activeTextColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.label,
                            fontSize = 11.sp,
                            style = MaterialTheme.typography.labelSmall,
                            color = activeTextColor
                        )
                    }
                }
            }
        }
    }
}
