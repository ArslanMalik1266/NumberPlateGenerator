package com.webscare.numberplategenerator.ui.editor.canvas

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback

import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.webscare.numberplategenerator.data.datasource.AssetDataProvider
import com.webscare.numberplategenerator.domain.model.EditorTabType
import com.webscare.numberplategenerator.ui.EditorStates
import com.webscare.numberplategenerator.ui.HeaderAlignment
import com.webscare.numberplategenerator.ui.PlateType
import com.webscare.numberplategenerator.ui.ToolState
import com.webscare.numberplategenerator.ui.theme.bg_color
import com.webscare.numberplategenerator.ui.theme.black_color
import com.webscare.numberplategenerator.ui.theme.white_color
import com.webscare.numberplategenerator.utils.loadFontFromUrl

@Composable
fun PlateCanvasPreview(
    state: EditorStates,
    externalRotation: Float,
    onLongPressAction: (Boolean) -> Unit,
    onHeaderMove: (Offset) -> Unit,
    onHeaderReset: () -> Unit,
    onHeaderDropped: (Offset, Size) -> Unit,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val isBike = state.plateType == PlateType.BIKE
    val aspectRatio = if (isBike) 1.33f else 3.27f

    // --- 1. States & Data ---
    val bgState = state.toolStates[EditorTabType.HEADER] as? ToolState.BackgroundState
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
            AssetDataProvider.plateDimensions
                .find { it.id == currentId && it.plateType == state.plateType }
                ?: AssetDataProvider.plateDimensions
                    .first { it.plateType == state.plateType }
        }
    val dynamicAspectRatio = remember(selectedDim) {
        selectedDim.width / selectedDim.height
    }
    val hasOwnerName = nameState.ownerName.isNotEmpty()


    val selectedBg =
        AssetDataProvider.plateBackgrounds
            .find { it.id == bgState.selectedBackgroundId }
            ?: AssetDataProvider.plateBackgrounds.first()

    val fontUrl = textState?.selectedFont?.fontUrl
    val customFontFamily = loadFontFromUrl(fontUrl)

    val processedPlateText = remember(state.plateText, isBike, dynamicAspectRatio) {
        if (dynamicAspectRatio < 2.0f) {
            // Square plate: alphabets aur numbers alag lines mein
            // Example: "LEB-2847" → "LEB\n2847" ya "ABC 123" → "ABC\n123"
            val text = state.plateText.trim()

            // Pehla number dhundo
            val firstDigitIndex = text.indexOfFirst { it.isDigit() }

            if (firstDigitIndex > 0) {
                // Agar hyphen ya space pehle aaye to usey bhi hata do
                val prefix = text.substring(0, firstDigitIndex).trimEnd('-', ' ')
                val suffix = text.substring(firstDigitIndex).trimStart('-', ' ')
                "$prefix\n$suffix"
            } else {
                text // Koi digit nahi mili to same rakhو
            }
        } else {
            // Lambi plate: same line
            state.plateText
        }
    }

    // --- 2. PAINTERS (Inhein Canvas se bahar rakhna lazmi hai) ---
    val flagPainter = flagState?.selectedFlagRes?.let { painterResource(id = it) }
    val stickerPainter = stickerState?.selectedStickerRes?.let { painterResource(id = it) }
    val isSquareStyle = dynamicAspectRatio < 2.0f
    val haptic = LocalHapticFeedback.current
    var touchPosition by remember { mutableStateOf(Offset.Zero) }

    val headerHeightFactor = if (isSquareStyle) 0.20f else 0.35f
    var isLocallyDragging by remember { mutableStateOf(false) }
    var lastStablePosition by remember { mutableStateOf(Offset.Zero) }
    var headerLeft by remember { mutableStateOf(0f) }
    var headerTop by remember { mutableStateOf(0f) }
    var headerRight by remember { mutableStateOf(0f) }
    var headerBottom by remember { mutableStateOf(0f) }

    val headerOffset by animateOffsetAsState(
        targetValue = state.headerDragOffset,
        animationSpec = tween(
            durationMillis = if (state.isLongPressingHeader) 0 else 400, // Dragging ke waqt 0ms delay (no animation)
            easing = FastOutSlowInEasing
        ),
        label = "HeaderMovement"
    )




    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .background(bg_color)
            .height(280.dp)
            .aspectRatio(dynamicAspectRatio)
            .padding(16.dp)
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { offset ->
                        println("🔴arslan DRAG START at: $offset")
                        touchPosition = offset

                        // ✅ Area nahi, ACTUAL header bounds check ho rahi hain
                        val isInsideHeader = offset.x >= headerLeft &&
                                offset.x <= headerRight &&
                                offset.y >= headerTop &&
                                offset.y <= headerBottom

                        println("🔴arslan Header bounds: ($headerLeft,$headerTop) to ($headerRight,$headerBottom)")
                        println("🔴arslan Touch: ${offset.x}, ${offset.y} → Inside: $isInsideHeader")

                        if (isInsideHeader) {
                            println("✅arslan INSIDE HEADER - Starting drag")
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            isLocallyDragging = true
                            lastStablePosition = offset
                            onLongPressAction(true)
                        } else {
                            println("❌arslan OUTSIDE HEADER")
                        }
                    },
                    onDrag = { change, dragAmount ->
                        println("🟢arslan DRAG DETECTED: $dragAmount, IsLocallyDragging: $isLocallyDragging")

                        if (isLocallyDragging) {
                            change.consume()
                            touchPosition = change.position

                            // ✅ Clamp position within plate bounds
                            lastStablePosition = Offset(
                                x = change.position.x.coerceIn(0f, size.width.toFloat()),
                                y = change.position.y.coerceIn(0f, size.height.toFloat())
                            )

                            println("✅ Moving header by: $dragAmount, clamped pos: $lastStablePosition")
                            onHeaderMove(dragAmount)
                        }
                    },
                    onDragEnd = {
                        println("🔵arslan DRAG END at: $lastStablePosition")
                        if (isLocallyDragging && lastStablePosition != Offset.Zero) {
                            onHeaderDropped(lastStablePosition, size.toSize())
                        }
                        // ✅ Reset everything
                        isLocallyDragging = false
                        touchPosition = Offset.Zero
                        lastStablePosition = Offset.Zero  // ✅ was missing before
                        onHeaderReset()
                    },
                    onDragCancel = {
                        println("🟡 arslan DRAG CANCEL")
                        isLocallyDragging = false
                        touchPosition = Offset.Zero
                        lastStablePosition = Offset.Zero  // ✅ reset here too
                        onHeaderReset()
                    }
                )
            }
            .graphicsLayer {
                rotationY = externalRotation
                cameraDistance = 12f * density

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
            val sideGuideWidth = size.width * 0.3f

            val guideWidth = size.width
            // In variables ko replace karein:
            val isInTopZone = touchPosition.y <= headerHeight && touchPosition != Offset.Zero
            val isInLeftSide = touchPosition.x < sideGuideWidth && touchPosition != Offset.Zero
            val isInRightSide = touchPosition.x > (size.width - sideGuideWidth) && touchPosition != Offset.Zero
            val isInSideZone = (isInLeftSide || isInRightSide || isInTopZone) && isLocallyDragging

            val activeZone = when {
                isInRightSide && isInTopZone -> {
                    when (state.headerAlignment) {
                        HeaderAlignment.RIGHT -> HeaderAlignment.TOP   // RIGHT par tha → TOP prefer
                        HeaderAlignment.TOP -> HeaderAlignment.RIGHT   // TOP par tha → RIGHT prefer
                        else -> HeaderAlignment.TOP
                    }
                }
                isInLeftSide && isInTopZone -> {
                    when (state.headerAlignment) {
                        HeaderAlignment.LEFT -> HeaderAlignment.TOP    // LEFT par tha → TOP prefer
                        HeaderAlignment.TOP -> HeaderAlignment.LEFT    // TOP par tha → LEFT prefer
                        else -> HeaderAlignment.TOP
                    }
                }
                isInRightSide -> HeaderAlignment.RIGHT
                isInLeftSide -> HeaderAlignment.LEFT
                else -> HeaderAlignment.TOP
            }

            val dynamicWidth: Float
            val dynamicHeight: Float
            if (isLocallyDragging) {
                when (activeZone) {
                    HeaderAlignment.LEFT, HeaderAlignment.RIGHT -> {
                        dynamicWidth = sideGuideWidth
                        dynamicHeight = size.height
                    }
                    HeaderAlignment.TOP -> {
                        dynamicWidth = size.width
                        dynamicHeight = headerHeight
                    }
                }
            } else {
                when (state.headerAlignment) {
                    HeaderAlignment.LEFT, HeaderAlignment.RIGHT -> {
                        dynamicWidth = sideGuideWidth
                        dynamicHeight = size.height
                    }
                    HeaderAlignment.TOP -> {
                        dynamicWidth = size.width
                        dynamicHeight = headerHeight
                    }
                }
            }
            val finalTranslateX: Float
            val finalTranslateY: Float

            if (isLocallyDragging) {
                // Jab shape shift ho, toh finger ko center mein rakhne ke liye calculation
                // Hum touchPosition se half width/height minus karte hain
                finalTranslateX = (touchPosition.x - (dynamicWidth / 2)).coerceIn(0f, size.width - dynamicWidth)
                finalTranslateY = (touchPosition.y - (dynamicHeight / 2)).coerceIn(0f, size.height - dynamicHeight)
            } else {
                finalTranslateX = when (state.headerAlignment) {
                    HeaderAlignment.LEFT -> 0f
                    HeaderAlignment.RIGHT -> size.width - dynamicWidth
                    HeaderAlignment.TOP -> 0f
                }
                finalTranslateY = when (state.headerAlignment) {
                    HeaderAlignment.LEFT -> 0f
                    HeaderAlignment.RIGHT -> 0f
                    HeaderAlignment.TOP -> 0f
                }
            }
            headerLeft = finalTranslateX
            headerTop = finalTranslateY
            headerRight = finalTranslateX + dynamicWidth
            headerBottom = finalTranslateY + dynamicHeight

            val remainingHeight = size.height - headerHeight
            val cornerRadiusPx = 8.dp.toPx()
            // --- STEP 2: HEADER RECTANGLE ---
            withTransform({
                translate(left = finalTranslateX, top = finalTranslateY)
            }) {

                val headerPath = Path().apply {
                    addRoundRect(
                        roundRect = RoundRect(
                            left = 0f,
                            top = 0f,
                            right = dynamicWidth,
                            bottom = dynamicHeight,
                        )
                    )
                }
                drawPath(path = headerPath, color = selectedBg.headerColor)
                val isVertical = if (isLocallyDragging) {
                    activeZone == HeaderAlignment.LEFT || activeZone == HeaderAlignment.RIGHT
                } else {
                    state.headerAlignment == HeaderAlignment.LEFT || state.headerAlignment == HeaderAlignment.RIGHT
                }

                // --- STEP 3: HEADER TEXT ---
                val hPadding = 4.dp.toPx()
                val vPadding = if (isVertical) 32.dp.toPx() else 4.dp.toPx()
                var currentFontSize = if (isVertical) 14f else 14f
                val minFontSize = 6f
                val maxWidthSpace = (dynamicWidth - (hPadding * 2)).coerceAtLeast(1f)
                val maxHeightSpace = (dynamicHeight - (vPadding * 2)).coerceAtLeast(1f)
                var headerTextLayout = textMeasurer.measure(
                    text = selectedBg.headerText,
                    style = TextStyle(
                        fontSize = currentFontSize.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = if (isVertical) 0.sp else 1.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = currentFontSize.sp
                    ),
                    constraints = androidx.compose.ui.unit.Constraints(
                        maxWidth = maxWidthSpace.toInt()
                    )
                )
                while (
                    (headerTextLayout.size.height > maxHeightSpace || headerTextLayout.size.width > maxWidthSpace)
                    && currentFontSize > minFontSize
                ) {
                    currentFontSize -= 0.5f
                    headerTextLayout = textMeasurer.measure(
                        text = selectedBg.headerText,
                        style = TextStyle(
                            fontSize = currentFontSize.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = if (isVertical) 0.sp else 0.5.sp,
                            textAlign = TextAlign.Center
                        ),
                        constraints = androidx.compose.ui.unit.Constraints(
                            maxWidth = maxWidthSpace.toInt()
                        )
                    )
                }

                val textX = (dynamicWidth - headerTextLayout.size.width) / 2
                val textY = (dynamicHeight - headerTextLayout.size.height) / 2
                    drawText(
                        textLayoutResult = headerTextLayout,
                        topLeft = Offset(textX, textY)
                    )
// --- STEP 6: STICKER DRAWING (Adaptive Logic) ---
                    stickerPainter?.let { painter ->
                        val tintColor =
                            stickerState?.stickerTint?.let { Color(it) } ?: Color.Transparent

                        val stickerSizePx = headerHeight * 0.7f
                        // Position logic:
                        // Wide plate -> Header ke right side par
                        // Square plate -> Plate ke center-left area mein (Center text ke sath alignment)
                        val stickerX = if (isVertical) {
                            (dynamicWidth - stickerSizePx) / 2
                        } else {
                            size.width - stickerSizePx - 12.dp.toPx()
                        }

                        val stickerY = if (isVertical) {
                            dynamicHeight - stickerSizePx - 4.dp.toPx()
                        } else {
                            (headerHeight - stickerSizePx) / 2
                        }

                        translate(left = stickerX, top = stickerY) {
                            with(painter) {
                                draw(
                                    size = Size(stickerSizePx, stickerSizePx),
                                    colorFilter = ColorFilter.tint(tintColor)
                                )
                            }
                        }
                    }

                    // --- STEP 5: FLAG DRAWING ---
                    // --- STEP 5: FLAG DRAWING (Set and Scaled) ---
                    flagPainter?.let { painter ->
                        // Flag ki height header ke mutabiq 60% rakhein taake padding nazar aaye
                        val flagHeight = headerHeight * 0.6f
                        val flagWidth = flagHeight * 1.4f // Standard flag aspect ratio


                        val flagX = if (isVertical) (dynamicWidth - flagWidth) / 2 else 12.dp.toPx()
                        val flagY = if (isVertical) 8.dp.toPx() else (dynamicHeight - flagHeight) / 2

                        translate(
                            left = flagX, top = flagY
                        ) {
                            with(painter) {
                                draw(size = Size(flagWidth, flagHeight))
                            }
                        }
                    }




            }
            // --- STEP 3: MAIN TEXT ---

            val safeGap = 8.dp.toPx()
            val isHeaderVertical = if (isLocallyDragging) {
                activeZone == HeaderAlignment.LEFT || activeZone == HeaderAlignment.RIGHT
            } else {
                state.headerAlignment == HeaderAlignment.LEFT || state.headerAlignment == HeaderAlignment.RIGHT
            }
            val currentAlignment = if (isLocallyDragging) activeZone else state.headerAlignment

// Owner name ke liye space reserve karein (e.g., 20dp equivalent pixels)
            val ownerAreaHeight = if (hasOwnerName) 22.dp.toPx() else 0f
            var currentFontSizeValue =
                if (isSquareStyle) (size.height * 0.25f) else (size.height * 0.30f)

// Dynamic Font Size jo area mein fit aa jaye
            val dynamicMainFontSize = if (isBike) 60.sp else {
                if (hasOwnerName) 22.sp else 28.sp
            }
            val mainAreaLeft = if (currentAlignment  == HeaderAlignment.LEFT) dynamicWidth else 0f
            val mainAreaRight = if (currentAlignment  == HeaderAlignment.RIGHT) size.width - dynamicWidth else size.width
            val mainAreaTop = if (isHeaderVertical) 0f else headerHeight
            val availableWidth = mainAreaRight - mainAreaLeft
            val availableHeight = if (isHeaderVertical) size.height else size.height - headerHeight - safeGap




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
            while ((mainTextLayout.size.height > availableHeight ||
                        mainTextLayout.size.width > availableWidth)
                && iterations < 3) {
                currentFontSizeValue *= 0.90f
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
            val mainX = mainAreaLeft + (availableWidth - mainTextLayout.size.width) / 2
            val mainY = mainAreaTop  + (availableHeight  - mainTextLayout.size.height) / 2
            drawText(
                textLayoutResult = mainTextLayout,
                topLeft = Offset(mainX, mainY)
            )

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

                val ownerX = mainAreaLeft + (availableWidth - ownerTextLayout.size.width) / 2
                val ownerYFromBottom = mainAreaTop + availableHeight - ownerTextLayout.size.height - 6.dp.toPx()
                val ownerYFromText = mainY + mainTextLayout.size.height + 4.dp.toPx()
                val ownerY = maxOf(ownerYFromBottom, ownerYFromText)

                drawText(
                    textLayoutResult = ownerTextLayout,
                    topLeft = Offset(x = ownerX, y = ownerY)
                )
            }


            // --- FINAL BORDER ---
            drawRoundRect(
                color = black_color.copy(alpha = 0.1f),
                size = size,
                cornerRadius = CornerRadius(8.dp.toPx()),
                style = Stroke(width = 2.dp.toPx())
            )

            // --- PLACEMENT EFFECT ---
            if (isLocallyDragging || state.isLongPressingHeader) {
                val guideWidth = size.width * 0.3f
                val guideAlpha = 0.3f
                val strokeWidth = 1.5.dp.toPx()
                val greenColor = Color(0xFF4CAF50)
                val dashEffect = dashPathEffect(floatArrayOf(15f, 15f), 0f)



                // LEFT zone — sirf tab dikhao jab header already LEFT par NA ho

                    drawRect(
                        color = greenColor.copy(alpha = guideAlpha),
                        topLeft = Offset(0f, 0f),
                        size = Size(guideWidth, size.height)
                    )
                    drawRect(
                        color = greenColor,
                        topLeft = Offset(0f, 0f),
                        size = Size(guideWidth, size.height),
                        style = Stroke(width = strokeWidth, pathEffect = dashEffect)
                    )


                // RIGHT zone — sirf tab dikhao jab header already RIGHT par NA ho

                    drawRect(
                        color = greenColor.copy(alpha = guideAlpha),
                        topLeft = Offset(size.width - guideWidth, 0f),
                        size = Size(guideWidth, size.height)
                    )
                    drawRect(
                        color = greenColor,
                        topLeft = Offset(size.width - guideWidth, 0f),
                        size = Size(guideWidth, size.height),
                        style = Stroke(width = strokeWidth, pathEffect = dashEffect)
                    )


                // TOP zone — sirf tab dikhao jab header already TOP par NA ho

                    drawRect(
                        color = greenColor.copy(alpha = guideAlpha),
                        topLeft = Offset(0f, 0f),
                        size = Size(size.width, headerHeight)
                    )
                    drawRect(
                        color = greenColor,
                        topLeft = Offset(0f, 0f),
                        size = Size(size.width, headerHeight),
                        style = Stroke(width = strokeWidth, pathEffect = dashEffect)
                    )

            }
        }
    }
}