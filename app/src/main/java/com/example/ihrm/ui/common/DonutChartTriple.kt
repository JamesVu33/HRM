package com.example.ihrm.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.min

/**
 * Multi-segment donut chart with thin gaps between arcs (background shows through),
 * flat stroke caps, ring inset inside the canvas — aligned with Figma pie charts.
 *
 * @param fractions Raw segment weights (e.g. counts); normalized internally by sum.
 * @param colors One color per segment; size must match [fractions].
 */
@Composable
fun DonutChartTriple(
    modifier: Modifier = Modifier,
    fractions: List<Float>,
    colors: List<Color>,
    strokeWidth: Dp
) {
    Canvas(modifier = modifier) {
        val n = fractions.size
        if (n == 0 || colors.size != n) return@Canvas

        val sum = fractions.sum()
        if (sum <= 0f) return@Canvas
        val normalized = fractions.map { it / sum }

        val gapDegrees = 3.2f
        val totalGaps = n * gapDegrees
        val sweepBudget = (360f - totalGaps).coerceAtLeast(0f)
        if (sweepBudget <= 0f) return@Canvas

        val padH = size.width * 0.175f
        val padV = size.height * 0.0667f
        val innerW = (size.width - 2f * padH).coerceAtLeast(0f)
        val innerH = (size.height - 2f * padV).coerceAtLeast(0f)
        val diameter = min(innerW, innerH)
        if (diameter <= 0f) return@Canvas

        val left = padH + (innerW - diameter) / 2f
        val top = padV + (innerH - diameter) / 2f
        val topLeft = Offset(left, top)
        val arcSize = Size(diameter, diameter)

        val strokePx = strokeWidth.toPx()
        val strokeStyle = Stroke(width = strokePx, cap = StrokeCap.Butt)

        var startAngle = -90f
        normalized.zip(colors).forEach { (fraction, color) ->
            val sweep = fraction * sweepBudget
            if (sweep > 0.05f) {
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = strokeStyle
                )
            }
            startAngle += sweep + gapDegrees
        }
    }
}
