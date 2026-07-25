package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.ui.theme.LunaAccent
import com.example.ui.theme.LunaPrimary
import com.example.ui.theme.LunaSecondary
import kotlin.math.cos
import kotlin.math.sin

/**
 * A subtle 'Geometric Balance' background animation layer using Compose Canvas.
 * Renders smooth, shifting organic shapes, flowing Bezier paths, and floating ambient nodes.
 */
@Composable
fun GeometricBalanceBackground(
    modifier: Modifier = Modifier,
    alphaMultiplier: Float = 0.15f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "GeometricBalanceTransition")

    val phase1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase1"
    )

    val phase2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase2"
    )

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val primaryColor = LunaPrimary.copy(alpha = alphaMultiplier)
    val secondaryColor = LunaSecondary.copy(alpha = alphaMultiplier * 0.8f)
    val accentColor = LunaAccent.copy(alpha = alphaMultiplier * 0.6f)
    val outlineColor = MaterialTheme.colorScheme.primary.copy(alpha = alphaMultiplier * 0.4f)

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        if (width <= 0 || height <= 0) return@Canvas

        // 1. Shifting Organic Wave Path 1 (Top / Upper Right)
        val path1 = Path().apply {
            val startY = height * 0.15f + sin(phase1) * 30f
            moveTo(0f, startY)

            val cp1X = width * 0.35f + cos(phase1) * 50f
            val cp1Y = height * 0.05f + sin(phase2) * 60f
            val cp2X = width * 0.7f + sin(phase1) * 40f
            val cp2Y = height * 0.28f + cos(phase2) * 50f
            val endX = width
            val endY = height * 0.18f + sin(phase2) * 30f

            cubicTo(cp1X, cp1Y, cp2X, cp2Y, endX, endY)
            lineTo(width, 0f)
            lineTo(0f, 0f)
            close()
        }

        drawPath(
            path = path1,
            brush = Brush.verticalGradient(
                colors = listOf(primaryColor, Color.Transparent),
                startY = 0f,
                endY = height * 0.35f
            )
        )

        // 2. Shifting Organic Wave Path 2 (Bottom / Center Flow)
        val path2 = Path().apply {
            val startY = height * 0.85f + cos(phase2) * 40f
            moveTo(0f, startY)

            val cp1X = width * 0.3f + sin(phase2) * 60f
            val cp1Y = height * 0.72f + cos(phase1) * 50f
            val cp2X = width * 0.75f + cos(phase1) * 50f
            val cp2Y = height * 0.92f + sin(phase2) * 40f
            val endX = width
            val endY = height * 0.8f + sin(phase1) * 30f

            cubicTo(cp1X, cp1Y, cp2X, cp2Y, endX, endY)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }

        drawPath(
            path = path2,
            brush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, secondaryColor),
                startY = height * 0.65f,
                endY = height
            )
        )

        // 3. Floating Geometric Balance Orbs
        val orb1CenterX = width * 0.8f + cos(phase1) * 35f
        val orb1CenterY = height * 0.25f + sin(phase1) * 45f
        val orb1Radius = (width * 0.35f) * pulse

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(secondaryColor, Color.Transparent),
                center = Offset(orb1CenterX, orb1CenterY),
                radius = orb1Radius
            ),
            center = Offset(orb1CenterX, orb1CenterY),
            radius = orb1Radius
        )

        val orb2CenterX = width * 0.2f + sin(phase2) * 40f
        val orb2CenterY = height * 0.65f + cos(phase2) * 50f
        val orb2Radius = (width * 0.4f) * (2f - pulse)

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(accentColor, Color.Transparent),
                center = Offset(orb2CenterX, orb2CenterY),
                radius = orb2Radius
            ),
            center = Offset(orb2CenterX, orb2CenterY),
            radius = orb2Radius
        )

        // 4. Delicate Geometric Concentric Balance Lines
        val centerX = width * 0.5f + cos(phase1 * 0.5f) * 20f
        val centerY = height * 0.45f + sin(phase2 * 0.5f) * 25f

        for (i in 1..3) {
            val circleRadius = (width * 0.22f * i) + sin(phase1 + i) * 15f
            drawCircle(
                color = outlineColor,
                radius = circleRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 1.5f)
            )
        }
    }
}
