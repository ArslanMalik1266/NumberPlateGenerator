package com.webscare.numberplategenerator.ui.editor

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
    val textState = state.toolStates[EditorTabType.TEXT] as? ToolState.TextState
    val styleState = state.toolStates[EditorTabType.STYLE] as? ToolState.StyleState
        ?: ToolState.StyleState()
    val fontUrl = textState?.selectedFont?.fontUrl
    val customFontFamily = loadFontFromUrl(fontUrl)
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
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            // Use a Box to layer the Text over the Canvas
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Empty for now
                }

                // Displaying the text from state
                Text(
                    text = state.plateText,
                    fontSize = if (state.plateType == PlateType.BIKE) 32.sp else 45.sp,
                    fontWeight = if (styleState.isBold) FontWeight.Bold else FontWeight.Normal,
                    fontStyle = if (styleState.isItalic) FontStyle.Italic else FontStyle.Normal,
                    textDecoration = if (styleState.isUnderline) TextDecoration.Underline else TextDecoration.None,
                    fontFamily = customFontFamily,
                    color = Color(styleState.selectedColor),
                    letterSpacing = 2.sp
                )

            }
        }
    }
}