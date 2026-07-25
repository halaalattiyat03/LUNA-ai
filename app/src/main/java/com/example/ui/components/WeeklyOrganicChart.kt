package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class WeeklyDataPoint(
    val day: String,
    val waterLiters: Float, // e.g. 2.4L (Goal: 2.5L)
    val sleepHours: Float   // e.g. 7.8h (Goal: 8.0h)
)

enum class ChartMetricFilter {
    BOTH, WATER_ONLY, SLEEP_ONLY
}

@Composable
fun WeeklyOrganicProgressChart(
    modifier: Modifier = Modifier
) {
    val sampleData = remember {
        listOf(
            WeeklyDataPoint("Mon", waterLiters = 2.1f, sleepHours = 7.2f),
            WeeklyDataPoint("Tue", waterLiters = 2.6f, sleepHours = 8.1f),
            WeeklyDataPoint("Wed", waterLiters = 2.3f, sleepHours = 7.6f),
            WeeklyDataPoint("Thu", waterLiters = 2.8f, sleepHours = 8.4f),
            WeeklyDataPoint("Fri", waterLiters = 2.0f, sleepHours = 6.9f),
            WeeklyDataPoint("Sat", waterLiters = 2.7f, sleepHours = 8.5f),
            WeeklyDataPoint("Sun", waterLiters = 2.5f, sleepHours = 8.0f)
        )
    }

    var selectedMetric by remember { mutableStateOf(ChartMetricFilter.BOTH) }
    var selectedPointIndex by remember { mutableStateOf<Int?>(6) } // Default to Sunday (latest)

    // Smooth Entrance Animation Progress (0f -> 1f)
    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing)
        )
    }

    // Color definitions
    val waterColorPrimary = Color(0xFF00B4D8)
    val waterColorSecondary = Color(0xFF90E0EF)
    val sleepColorPrimary = Color(0xFF7209B7)
    val sleepColorSecondary = Color(0xFFB5179E)

    GlassCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("weekly_organic_progress_chart")
        ) {
            // Header Row with Title and Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            tint = LunaPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Weekly Hydration & Sleep Curve",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "Organic continuous consistency trends",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = LunaPrimary.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "94% Score",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = LunaPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Metric Filter Selector Chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                ChartMetricChip(
                    label = "Both Metrics",
                    icon = null,
                    isSelected = selectedMetric == ChartMetricFilter.BOTH,
                    activeColor = LunaPrimary,
                    onClick = { selectedMetric = ChartMetricFilter.BOTH }
                )

                ChartMetricChip(
                    label = "Water (L)",
                    icon = Icons.Outlined.WaterDrop,
                    isSelected = selectedMetric == ChartMetricFilter.WATER_ONLY,
                    activeColor = waterColorPrimary,
                    onClick = { selectedMetric = ChartMetricFilter.WATER_ONLY }
                )

                ChartMetricChip(
                    label = "Sleep (h)",
                    icon = Icons.Outlined.Bedtime,
                    isSelected = selectedMetric == ChartMetricFilter.SLEEP_ONLY,
                    activeColor = sleepColorPrimary,
                    onClick = { selectedMetric = ChartMetricFilter.SLEEP_ONLY }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Interactive Bezier Area Canvas Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures { tapOffset ->
                                val width = size.width
                                val stepX = width / (sampleData.size - 1)
                                val index = ((tapOffset.x + stepX / 2) / stepX).toInt().coerceIn(0, sampleData.size - 1)
                                selectedPointIndex = index
                            }
                        }
                ) {
                    val width = size.width
                    val height = size.height
                    val bottomPadding = 30f
                    val topPadding = 20f
                    val chartHeight = height - bottomPadding - topPadding

                    val maxWater = 3.2f // Max scale for water (0 - 3.2L)
                    val maxSleep = 10f  // Max scale for sleep (0 - 10h)
                    val count = sampleData.size
                    val stepX = width / (count - 1)

                    val animProgress = animationProgress.value

                    // Draw subtle grid guide lines
                    val gridLines = 3
                    for (i in 0..gridLines) {
                        val y = topPadding + (chartHeight / gridLines) * i
                        drawLine(
                            color = Color.Gray.copy(alpha = 0.15f),
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )
                    }

                    // Water Data Points calculation
                    val waterPoints = sampleData.mapIndexed { index, data ->
                        val x = index * stepX
                        val normalizedVal = (data.waterLiters / maxWater).coerceIn(0f, 1f)
                        val y = topPadding + chartHeight * (1f - normalizedVal * animProgress)
                        Offset(x, y)
                    }

                    // Sleep Data Points calculation
                    val sleepPoints = sampleData.mapIndexed { index, data ->
                        val x = index * stepX
                        val normalizedVal = (data.sleepHours / maxSleep).coerceIn(0f, 1f)
                        val y = topPadding + chartHeight * (1f - normalizedVal * animProgress)
                        Offset(x, y)
                    }

                    // 1. DRAW WATER ORGANIC BEZIER PATH & GRADIENT FILL
                    if (selectedMetric == ChartMetricFilter.BOTH || selectedMetric == ChartMetricFilter.WATER_ONLY) {
                        val waterStrokePath = createSmoothPath(waterPoints)
                        val waterFillPath = Path().apply {
                            addPath(waterStrokePath)
                            lineTo(width, height - bottomPadding)
                            lineTo(0f, height - bottomPadding)
                            close()
                        }

                        // Gradient Fill under Water Curve
                        drawPath(
                            path = waterFillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    waterColorPrimary.copy(alpha = 0.35f),
                                    waterColorSecondary.copy(alpha = 0.05f)
                                ),
                                startY = topPadding,
                                endY = height - bottomPadding
                            )
                        )

                        // Water Organic Contour Line
                        drawPath(
                            path = waterStrokePath,
                            brush = Brush.horizontalGradient(listOf(waterColorPrimary, waterColorSecondary)),
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Water Data Points
                        waterPoints.forEachIndexed { idx, point ->
                            val isSelected = selectedPointIndex == idx
                            val pointRadius = if (isSelected) 6.dp.toPx() else 4.dp.toPx()

                            drawCircle(
                                color = Color.White,
                                radius = pointRadius,
                                center = point
                            )
                            drawCircle(
                                color = waterColorPrimary,
                                radius = pointRadius - 1.5.dp.toPx(),
                                center = point
                            )
                        }
                    }

                    // 2. DRAW SLEEP ORGANIC BEZIER PATH & GRADIENT FILL
                    if (selectedMetric == ChartMetricFilter.BOTH || selectedMetric == ChartMetricFilter.SLEEP_ONLY) {
                        val sleepStrokePath = createSmoothPath(sleepPoints)
                        val sleepFillPath = Path().apply {
                            addPath(sleepStrokePath)
                            lineTo(width, height - bottomPadding)
                            lineTo(0f, height - bottomPadding)
                            close()
                        }

                        // Gradient Fill under Sleep Curve
                        drawPath(
                            path = sleepFillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    sleepColorPrimary.copy(alpha = 0.30f),
                                    sleepColorSecondary.copy(alpha = 0.03f)
                                ),
                                startY = topPadding,
                                endY = height - bottomPadding
                            )
                        )

                        // Sleep Organic Contour Line
                        drawPath(
                            path = sleepStrokePath,
                            brush = Brush.horizontalGradient(listOf(sleepColorPrimary, sleepColorSecondary)),
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Sleep Data Points
                        sleepPoints.forEachIndexed { idx, point ->
                            val isSelected = selectedPointIndex == idx
                            val pointRadius = if (isSelected) 6.dp.toPx() else 4.dp.toPx()

                            drawCircle(
                                color = Color.White,
                                radius = pointRadius,
                                center = point
                            )
                            drawCircle(
                                color = sleepColorPrimary,
                                radius = pointRadius - 1.5.dp.toPx(),
                                center = point
                            )
                        }
                    }

                    // 3. DRAW SELECTED DAY VERTICAL INDICATOR LINE
                    selectedPointIndex?.let { idx ->
                        val targetX = idx * stepX
                        drawLine(
                            color = LunaPrimary.copy(alpha = 0.5f),
                            start = Offset(targetX, topPadding),
                            end = Offset(targetX, height - bottomPadding),
                            strokeWidth = 1.5.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // X-Axis Day Labels Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                sampleData.forEachIndexed { idx, item ->
                    val isSelected = selectedPointIndex == idx
                    Text(
                        text = item.day,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) LunaPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { selectedPointIndex = idx }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Selected Day Tooltip Detail Panel
            selectedPointIndex?.let { idx ->
                val selectedData = sampleData[idx]
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f),
                    border = BorderStroke(1.dp, LunaBorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${selectedData.day}'s Metrics",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(waterColorPrimary)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Water: ${selectedData.waterLiters}L / 2.5L",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = waterColorPrimary
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(sleepColorPrimary)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Sleep: ${selectedData.sleepHours}h / 8.0h",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = sleepColorPrimary
                                    )
                                }
                            }
                        }

                        val waterAchieved = selectedData.waterLiters >= 2.5f
                        val sleepAchieved = selectedData.sleepHours >= 8.0f

                        if (waterAchieved && sleepAchieved) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = LunaSuccess.copy(alpha = 0.2f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.CheckCircle,
                                        contentDescription = null,
                                        tint = LunaSuccess,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Goals Met",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LunaSuccess
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Creates a smooth organic Bezier path through a list of continuous offsets.
 */
private fun createSmoothPath(points: List<Offset>): Path {
    val path = Path()
    if (points.isEmpty()) return path

    path.moveTo(points.first().x, points.first().y)

    for (i in 0 until points.size - 1) {
        val p0 = points[i]
        val p1 = points[i + 1]

        val controlPoint1 = Offset(p0.x + (p1.x - p0.x) / 2f, p0.y)
        val controlPoint2 = Offset(p0.x + (p1.x - p0.x) / 2f, p1.y)

        path.cubicTo(
            controlPoint1.x, controlPoint1.y,
            controlPoint2.x, controlPoint2.y,
            p1.x, p1.y
        )
    }

    return path
}

@Composable
private fun ChartMetricChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector?,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) activeColor else MaterialTheme.colorScheme.surfaceVariant,
        border = if (isSelected) null else BorderStroke(1.dp, LunaBorderLight),
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = label,
                fontSize = 11.5.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
