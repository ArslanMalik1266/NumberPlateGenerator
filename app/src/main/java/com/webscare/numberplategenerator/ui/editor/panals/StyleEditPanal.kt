package com.webscare.numberplategenerator.ui.editor.panals

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplategenerator.data.datasource.StyleDataProvider
import com.webscare.numberplategenerator.domain.model.EditorTabType
import com.webscare.numberplategenerator.ui.ToolState
import com.webscare.numberplategenerator.ui.MainViewModel
import com.webscare.numberplategenerator.ui.theme.black_color
import com.webscare.numberplategenerator.ui.theme.grey_color
import com.webscare.numberplategenerator.ui.theme.white_color
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StyleEditPanel(
    viewModel: MainViewModel = koinViewModel()
) {
    val state by viewModel.editorState.collectAsState()
    val styleState = state.toolStates[EditorTabType.STYLE] as? ToolState.StyleState
        ?: ToolState.StyleState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) {
        item {
            Text(
                text = "TEXT STYLE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = grey_color
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StyleToggleButton(
                    label = "Bold",
                    icon = "B",
                    isSelected = styleState.isBold,
                    modifier = Modifier.weight(1f),
                    onClick = { }
                )
                StyleToggleButton(
                    label = "Italic",
                    icon = "I",
                    isSelected = styleState.isItalic,
                    isItalic = true,
                    modifier = Modifier.weight(1f),
                    onClick = { }
                )
                StyleToggleButton(
                    label = "Underline",
                    icon = "U",
                    isSelected = styleState.isUnderline,
                    isUnderline = true,
                    modifier = Modifier.weight(1f),
                    onClick = {}
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // --- 2. TEXT COLOR SECTION ---
        item {
            Text(
                text = "TEXT COLOR",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = grey_color
            )
            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StyleDataProvider.solidColors.forEach { colorInt ->
                    ColorOptionCircle(
                        colorInt = colorInt,
                        isSelected = styleState.selectedColor == colorInt,
                        onSelect = { viewModel.onColorSelect(colorInt) }
                    )
                }
            }
        }
    }
}

@Composable
fun StyleToggleButton(
    label: String,
    icon: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    isItalic: Boolean = false,
    isUnderline: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .background(
                color = white_color,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = if (isSelected) black_color else grey_color.copy(alpha = 0.15f),
                shape = RoundedCornerShape(16.dp)
            )

            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = icon,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
            textDecoration = if (isUnderline) TextDecoration.Underline else TextDecoration.None,
            color = black_color
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = black_color
        )
    }
}

@Composable
fun ColorOptionCircle(
    colorInt: Int,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Color(colorInt) else grey_color.copy(alpha = 0.2f),
                shape = CircleShape
            )
            .padding(4.dp)
            .background(Color(colorInt), CircleShape)
            .clickable { onSelect() }
    )
}