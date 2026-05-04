package com.webscare.numberplategenerator.ui.editor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplategenerator.domain.model.EditorTabType
import com.webscare.numberplategenerator.ui.EditorStates
import com.webscare.numberplategenerator.ui.PlateType
import com.webscare.numberplategenerator.ui.ToolState
import com.webscare.numberplategenerator.ui.theme.bg_color
import com.webscare.numberplategenerator.ui.theme.grey_color
import com.webscare.numberplategenerator.utils.loadFontFromUrl

@Composable
fun PlatePreviewContainer(
    state: EditorStates,
    modifier: Modifier = Modifier,
) {
    val bgState = state.toolStates[EditorTabType.BACKGROUND] as? ToolState.BackgroundState ?: ToolState.BackgroundState()
    val selectedBg = com.webscare.numberplategenerator.data.datasource.AssetDataProvider.plateBackgrounds
        .find { it.id == bgState.selectedBackgroundId } ?: com.webscare.numberplategenerator.data.datasource.AssetDataProvider.plateBackgrounds.first()
    val textState = state.toolStates[EditorTabType.TEXT] as? ToolState.TextState
    val styleState = state.toolStates[EditorTabType.STYLE] as? ToolState.StyleState
        ?: ToolState.StyleState()
    val nameState = state.toolStates[EditorTabType.NAME] as? ToolState.NameState
        ?: ToolState.NameState()
    val flagState = state.toolStates[EditorTabType.FLAG] as? ToolState.FlagState
    val stickerState = state.toolStates[EditorTabType.STICKER] as? ToolState.StickerState
    val fontUrl = textState?.selectedFont?.fontUrl
    val customFontFamily = loadFontFromUrl(fontUrl)
    val isBike = state.plateType == PlateType.BIKE

    val processedPlateText = remember(state.plateText, isBike) {
        if (isBike) {
            state.plateText.replace("-", "\n").replace(" ", "\n")
        } else {
            state.plateText
        }
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(bg_color)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val aspectRatio = when (state.plateType) {
            PlateType.BIKE -> 1.33f // More "square" for bikes (240x180)
            PlateType.CAR -> 3.27f  // Long and thin for cars (360x110)
        }

        Surface(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .heightIn(150.dp)
                .fillMaxWidth()
                .aspectRatio(aspectRatio),
            shape = RoundedCornerShape(8.dp),
            color = selectedBg.plateColor,
            shadowElevation = 8.dp
        )
        {
            Column(modifier = Modifier.fillMaxSize()) {

                // --- 1. TOP BAR (GOVERNMENT PART) ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(if (isBike) 0.18f else 0.4f)
                        .background(selectedBg.headerColor),
                    contentAlignment = Alignment.Center
                ) {
                    flagState?.selectedFlagRes?.let { flagRes ->
                        Image(
                            painter = androidx.compose.ui.res.painterResource(id = flagRes),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 8.dp)
                                .size(if (isBike) 26.dp else 22.dp)
                        )
                    }

                    if (!isBike) {
                        stickerState?.selectedStickerRes?.let { res ->
                            Image(
                                painter = androidx.compose.ui.res.painterResource(id = res),
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 12.dp)
                                    .size(22.dp),
                                colorFilter = ColorFilter.tint(androidx.compose.ui.graphics.Color(stickerState.stickerTint))
                            )
                        }
                    }
                    Text(
                        text = selectedBg.headerText,
                        color = Color.White,
                        fontSize = if (isBike) 14.sp else 12.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }
            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(0.65f),
                contentAlignment = Alignment.Center)
            {
                if (isBike) {
                    stickerState?.selectedStickerRes?.let { res ->
                        Image(
                            painter = androidx.compose.ui.res.painterResource(id = res),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 12.dp)
                                .size(35.dp),
                            colorFilter = ColorFilter.tint(androidx.compose.ui.graphics.Color(stickerState.stickerTint))
                        )
                    }
                }

                Text(
                    text = processedPlateText,
                    fontSize = if (isBike) 52.sp else 26.sp,
                    fontWeight = if (styleState.isBold) FontWeight.Black else FontWeight.Bold,
                    fontStyle = if (styleState.isItalic) FontStyle.Italic else FontStyle.Normal,
                    textDecoration = if (styleState.isUnderline) TextDecoration.Underline else TextDecoration.None,
                    fontFamily = customFontFamily,
                    color = Color(styleState.selectedColor),
                    letterSpacing = if (isBike) 1.sp else 4.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = if (isBike) 52.sp else 26.sp
                )
            }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                       , // Bike mein bottom space zyada hai
                    contentAlignment = Alignment.Center
                ) {
                    if (nameState.ownerName.isNotEmpty()) {
                        Text(
                            text = nameState.ownerName.uppercase(),
                            fontSize = if (isBike) 14.sp else 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(styleState.selectedColor),
                            letterSpacing = 2.sp
                        )
                    }
                }
            }
        }
    }
}