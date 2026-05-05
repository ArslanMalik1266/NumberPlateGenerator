package com.webscare.numberplategenerator.ui.editor.canva

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun PlateDimensionThumbnail(
    width: Float,
    height: Float,
    headerColor: Color,
    plateColor: Color,
    modifier: Modifier = Modifier
) {
    // Exact Aspect Ratio from input dimensions
    val aspectRatio = width / height
    val isBike = aspectRatio < 2.0f // Logic to match your PlateType check

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
    ) {
        // --- STEP 1: PLATE BASE (Exactly from your code) ---
        drawRoundRect(
            color = plateColor,
            size = size,
            cornerRadius = CornerRadius(4.dp.toPx()) // Scaled down corner for thumbnail
        )

        // --- STEP 2: HEADER RECTANGLE (Exactly from your code) ---
        // Bike logic: 18%, Car logic: 40% height
        val headerHeight = size.height * (if (isBike) 0.18f else 0.4f)

        drawRoundRect(
            color = headerColor,
            size = androidx.compose.ui.geometry.Size(width = size.width, height = headerHeight),
            cornerRadius = CornerRadius(4.dp.toPx())
        )

        // --- FINAL BORDER (For that professional look) ---
        drawRoundRect(
            color = Color.Black.copy(alpha = 0.1f),
            size = size,
            cornerRadius = CornerRadius(4.dp.toPx()),
            style = Stroke(width = 1.dp.toPx())
        )
    }
}