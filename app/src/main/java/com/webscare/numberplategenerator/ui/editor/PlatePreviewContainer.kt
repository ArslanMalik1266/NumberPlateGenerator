package com.webscare.numberplategenerator.ui.editor

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.PathEffect.Companion.dashPathEffect
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

import androidx.compose.ui.unit.sp
import com.webscare.numberplategenerator.domain.model.EditorTabType
import com.webscare.numberplategenerator.ui.EditorStates
import com.webscare.numberplategenerator.ui.PlateType
import com.webscare.numberplategenerator.ui.ToolState
import com.webscare.numberplategenerator.ui.theme.bg_color
import com.webscare.numberplategenerator.ui.theme.black_color
import com.webscare.numberplategenerator.ui.theme.white_color

@Composable
fun PlateCanvasPreview(
    state: EditorStates,
    externalRotation: Float,
    onLongPressAction: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val isBike = state.plateType == PlateType.BIKE
    val aspectRatio = if (isBike) 1.33f else 3.27f

    // --- 1. States & Data ---
    val bgState = state.toolStates[EditorTabType.BACKGROUND] as? ToolState.BackgroundState
        ?: ToolState.BackgroundState()
    val textState = state.toolStates[EditorTabType.TEXT] as? ToolState.TextState
    val styleState =
        state.toolStates[EditorTabType.STYLE] as? ToolState.StyleState ?: ToolState.StyleState()
    val flagState = state.toolStates[EditorTabType.FLAG] as? ToolState.FlagState
    val stickerState = state.toolStates[EditorTabType.STICKER] as? ToolState.StickerState
    val nameState =
        state.toolStates[EditorTabType.NAME] as? ToolState.NameState ?: ToolState.NameState()
    val dimState = state.toolStates[EditorTabType.DIMENSION] as? ToolState.DimensionState
        ?: ToolState.DimensionState()
    val selectedDim =
        remember(state.plateType, state.isFront, state.toolStates[EditorTabType.DIMENSION]) {
            val dimState = state.toolStates[EditorTabType.DIMENSION] as? ToolState.DimensionState

            val currentId = when (state.plateType) {
                PlateType.CAR -> if (state.isFront) dimState?.carFrontId else dimState?.carBackId
                PlateType.BIKE -> if (state.isFront) dimState?.bikeFrontId else dimState?.bikeBackId
            }
            com.webscare.numberplategenerator.data.datasource.AssetDataProvider.plateDimensions
                .find { it.id == currentId && it.plateType == state.plateType }
                ?: com.webscare.numberplategenerator.data.datasource.AssetDataProvider.plateDimensions
                    .first { it.plateType == state.plateType }
        }
    val dynamicAspectRatio = remember(selectedDim) {
        selectedDim.width / selectedDim.height
    }


    val selectedBg =
        com.webscare.numberplategenerator.data.datasource.AssetDataProvider.plateBackgrounds
            .find { it.id == bgState.selectedBackgroundId }
            ?: com.webscare.numberplategenerator.data.datasource.AssetDataProvider.plateBackgrounds.first()

    val fontUrl = textState?.selectedFont?.fontUrl
    val customFontFamily = com.webscare.numberplategenerator.utils.loadFontFromUrl(fontUrl)

    val processedPlateText = remember(state.plateText, isBike) {
        if (isBike) state.plateText
        else state.plateText
    }

    // --- 2. PAINTERS (Inhein Canvas se bahar rakhna lazmi hai) ---
    val flagPainter = flagState?.selectedFlagRes?.let { painterResource(id = it) }
    val stickerPainter = stickerState?.selectedStickerRes?.let { painterResource(id = it) }
    val isSquareStyle = dynamicAspectRatio < 2.0f

    val headerHeightFactor = if (isSquareStyle) 0.20f else 0.35f




    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .background(bg_color)
            .height(280.dp)
            .aspectRatio(dynamicAspectRatio)
            .padding(16.dp)
            .graphicsLayer {
                // YE APLLY KARNA HAI
                rotationY = externalRotation
                cameraDistance = 12f * density

            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { offset ->
                        val actualHeaderHeightPx = size.height * headerHeightFactor
                        // Check karein agar touch header area ke andar hai
                        if (offset.y <= actualHeaderHeightPx) {
                            onLongPressAction(true) // ViewModel function call karein
                        }
                    },
                    onPress = {
                        try {
                            awaitRelease()
                        } finally {
                            onLongPressAction(false) // Ungli uthate hi effect khatam
                        }
                    }
                )
            }

    ) {
        withTransform({
            if (externalRotation > 90f) {
                scale(scaleX = -1f, scaleY = 1f, pivot = center)
            }
        }) {
            // --- STEP 1: PLATE BASE ---
            drawRoundRect(
                color = white_color, // Aap yahan selectedBg.plateColor bhi use kar sakte hain
                size = size,
                cornerRadius = CornerRadius(8.dp.toPx())
            )
            val headerHeight = size.height * headerHeightFactor
            val remainingHeight = size.height - headerHeight

            // --- STEP 2: HEADER RECTANGLE ---
            val cornerRadiusPx = 8.dp.toPx()
            val headerPath = Path().apply {
                addRoundRect(
                    roundRect = RoundRect(
                        left = 0f,
                        top = 0f,
                        right = size.width,
                        bottom = headerHeight,
                        topLeftCornerRadius = CornerRadius(cornerRadiusPx),
                        topRightCornerRadius = CornerRadius(cornerRadiusPx),
                        bottomLeftCornerRadius = CornerRadius(0f),
                        bottomRightCornerRadius = CornerRadius(0f)
                    )
                )
            }
            drawPath(path = headerPath, color = selectedBg.headerColor)

            // --- STEP 3: HEADER TEXT ---
            val headerTextLayout = textMeasurer.measure(
                text = selectedBg.headerText,
                style = TextStyle(
                    fontSize = if (isSquareStyle) 14.sp else 12.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            )
            drawText(
                textLayoutResult = headerTextLayout,
                topLeft = Offset(
                    (size.width - headerTextLayout.size.width) / 2,
                    (headerHeight - headerTextLayout.size.height) / 2
                )
            )

            // --- STEP 3: MAIN TEXT ---
            val hasOwnerName = nameState.ownerName.isNotEmpty()
            val safeGap = 8.dp.toPx()

// Owner name ke liye space reserve karein (e.g., 20dp equivalent pixels)
            val ownerAreaHeight = if (hasOwnerName) 22.dp.toPx() else 0f
            val mainAreaHeight = size.height - headerHeight - safeGap
            var currentFontSizeValue =
                if (isSquareStyle) (size.height * 0.25f) else (size.height * 0.30f)

// Dynamic Font Size jo area mein fit aa jaye
            val dynamicMainFontSize = if (isBike) 60.sp else {
                if (hasOwnerName) 22.sp else 28.sp
            }

            // --- STEP 4: MAIN PLATE TEXT ---
            var mainTextLayout = textMeasurer.measure(
                text = processedPlateText,
                style = TextStyle(
                    fontSize = currentFontSizeValue.toSp(),
                    fontFamily = customFontFamily,
                    fontWeight = if (styleState.isBold) FontWeight.Black else FontWeight.Bold,
                    fontStyle = if (styleState.isItalic) FontStyle.Italic else FontStyle.Normal,
                    fontSynthesis = FontSynthesis.All,
                    color = Color(styleState.selectedColor),
                    letterSpacing = if (isBike) 1.sp else 4.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = currentFontSizeValue.toSp()
                )
            )
            var iterations = 0
            while (mainTextLayout.size.height > mainAreaHeight && iterations < 3) {
                currentFontSizeValue *= 0.95f // 20% chota karein har bar
                mainTextLayout = textMeasurer.measure(
                    text = processedPlateText,
                    style = TextStyle(
                        fontSize = currentFontSizeValue.toSp(),
                        fontFamily = customFontFamily,
                        fontWeight = if (styleState.isBold) FontWeight.Black else FontWeight.Bold,
                        fontStyle = if (styleState.isItalic) FontStyle.Italic else FontStyle.Normal,
                        color = Color(styleState.selectedColor),
                        letterSpacing = if (isBike) 1.sp else 4.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = (currentFontSizeValue * 1.1f).toSp()
                    )
                )
                iterations++
            }

            val mainY = headerHeight + (mainAreaHeight - mainTextLayout.size.height) / 2
            drawText(
                textLayoutResult = mainTextLayout,
                topLeft = Offset((size.width - mainTextLayout.size.width) / 2, mainY)
            )

            // --- STEP 5: FLAG DRAWING ---
            // --- STEP 5: FLAG DRAWING (Set and Scaled) ---
            flagPainter?.let { painter ->
                // Flag ki height header ke mutabiq 60% rakhein taake padding nazar aaye
                val flagHeight = headerHeight * 0.6f
                val flagWidth = flagHeight * 1.4f // Standard flag aspect ratio

                translate(
                    left = 12.dp.toPx(), // Left se constant padding
                    top = (headerHeight - flagHeight) / 2 // Vertical center in header
                ) {
                    with(painter) {
                        draw(size = Size(flagWidth, flagHeight))
                    }
                }
            }

// --- STEP 6: STICKER DRAWING (Adaptive Logic) ---
            stickerPainter?.let { painter ->
                val tintColor = stickerState?.stickerTint?.let { Color(it) } ?: Color.Transparent

                val stickerSizePx = headerHeight * 0.7f

                // Position logic:
                // Wide plate -> Header ke right side par
                // Square plate -> Plate ke center-left area mein (Center text ke sath alignment)
                val stickerX = size.width - stickerSizePx - 12.dp.toPx()
                val stickerY = (headerHeight - stickerSizePx) / 2

                translate(left = stickerX, top = stickerY) {
                    with(painter) {
                        draw(
                            size = Size(stickerSizePx, stickerSizePx),
                            colorFilter = ColorFilter.tint(tintColor)
                        )
                    }
                }
            }

            // --- STEP 7: OWNER NAME ---
            if (hasOwnerName) {
                val ownerFontSize = (size.height * 0.08f).toSp()

                val ownerTextLayout = textMeasurer.measure(
                    text = nameState.ownerName.uppercase(),
                    style = TextStyle(
                        fontSize = ownerFontSize,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(styleState.selectedColor),
                        letterSpacing = 2.sp,// Premium feel ke liye spacing
                        lineHeight = ownerFontSize * 1f
                    )
                )

                val ownerY = size.height - ownerTextLayout.size.height - 6.dp.toPx()

                drawText(
                    textLayoutResult = ownerTextLayout,
                    topLeft = Offset(
                        x = (size.width - ownerTextLayout.size.width) / 2,
                        y = ownerY
                    )
                )
            }

            // --- FINAL BORDER ---
            drawRoundRect(
                color = black_color.copy(alpha = 0.1f),
                size = size,
                cornerRadius = CornerRadius(8.dp.toPx()),
                style = Stroke(width = 2.dp.toPx())
            )

            // --- PLACEMENT EFFECT (Add this here) ---
            if (state.isLongPressingHeader) {
                val guideWidth = size.width * 0.15f // 15% of plate width
                val guideAlpha = 0.3f // Transparency level
                val strokeWidth = 1.5.dp.toPx()
                val greenColor = Color(0xFF4CAF50)
                val dashEffect = dashPathEffect(
                    floatArrayOf(15f, 15f), 0f
                )
                drawRect(
                    color = greenColor.copy(alpha = guideAlpha),
                    topLeft = Offset(0f, 0f),
                    size = Size(guideWidth, size.height)
                )
                // Left Border Line
                drawRect(
                    color = greenColor,
                    topLeft = Offset(0f, 0f),
                    size = Size(guideWidth, size.height),
                    style = Stroke(width = strokeWidth, pathEffect = dashEffect)
                )

                // 2. Right Side Guide (Full Height)
                drawRect(
                    color = greenColor.copy(alpha = guideAlpha),
                    topLeft = Offset(size.width - guideWidth, 0f),
                    size = Size(guideWidth, size.height)
                )
                // Right Border Line
                drawRect(
                    color = greenColor,
                    topLeft = Offset(size.width - guideWidth, 0f),
                    size = Size(guideWidth, size.height),
                    style = Stroke(width = strokeWidth, pathEffect = dashEffect)
                )
            }
        }
    }
}