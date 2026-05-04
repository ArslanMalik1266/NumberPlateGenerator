package com.webscare.numberplategenerator.ui.editor.panals

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplategenerator.data.datasource.AssetDataProvider
import com.webscare.numberplategenerator.domain.model.EditorTabType
import com.webscare.numberplategenerator.ui.MainViewModel
import com.webscare.numberplategenerator.ui.PlateBackground
import com.webscare.numberplategenerator.ui.PlateType
import com.webscare.numberplategenerator.ui.ToolState
import com.webscare.numberplategenerator.ui.theme.grey_color
import com.webscare.numberplategenerator.ui.theme.pink_color
import com.webscare.numberplategenerator.ui.theme.white_color
import com.webscare.numberplategenerator.utils.addPressEffect
import org.koin.compose.viewmodel.koinActivityViewModel

@Composable
fun BackgroundEditPanel(viewModel: MainViewModel = koinActivityViewModel()) {
    val state by viewModel.editorState.collectAsState()
    val bgState = state.toolStates[EditorTabType.BACKGROUND] as? ToolState.BackgroundState
        ?: ToolState.BackgroundState()
    val selectedBg = AssetDataProvider.plateBackgrounds.find { it.id == bgState.selectedBackgroundId }
        ?: AssetDataProvider.plateBackgrounds.first()
    val styleState = state.toolStates[EditorTabType.STYLE] as? ToolState.StyleState ?: ToolState.StyleState()
    val nameState = state.toolStates[EditorTabType.NAME] as? ToolState.NameState ?: ToolState.NameState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) {
        Text(
            text = "PLATE BACKGROUND",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = grey_color
        )
        val filteredBackgrounds = AssetDataProvider.plateBackgrounds.filter { it.type == state.plateType }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    )
    {
        items(filteredBackgrounds) { bg ->
            val isSelected = bg.id == bgState.selectedBackgroundId

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        // Selected hone par halka sa pinkish background (jaisa image mein hai)
                        if (isSelected) pink_color.copy(alpha = 0.08f) else white_color,
                        RoundedCornerShape(24.dp)
                    )
                    .border(
                        if (isSelected) 2.dp else 1.dp,
                        if (isSelected) pink_color else white_color,
                        RoundedCornerShape(16.dp)
                    )
                    .addPressEffect { viewModel.onBackgroundSelect(bg.id) }
                    .padding(12.dp),
                horizontalAlignment = Alignment.Start
            ) {

                // Dynamic Mini Preview
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    MiniPlatePreview(config = bg, plateText = state.plateText)
                }

                Spacer(Modifier.height(12.dp))

                Text(bg.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(bg.description, color = Color.Gray, fontSize = 11.sp)
            }
        }
    } }
}

@Composable
fun MiniPlatePreview(config: PlateBackground, plateText: String) {
    // 1. Ratio calculate karein (Car: ~3.27, Bike: ~1.33)
    val aspectRatio = if (config.type == PlateType.BIKE) 1.33f else 3.27f

    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f) // Card ke andar thora margin
            .aspectRatio(aspectRatio)
            .shadow(4.dp, RoundedCornerShape(4.dp))
            .background(config.plateColor, RoundedCornerShape(4.dp))
            .border(0.5.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
    ) {
        // 2. Header Bar (Dynamic Color and Text)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f) // Header height percentage
                .background(config.headerColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = config.headerText,
                color = Color.White,
                fontSize = if (config.type == PlateType.BIKE) 6.sp else 5.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                letterSpacing = 0.5.sp,
                lineHeight = if (config.type == PlateType.BIKE) 6.sp else 5.sp,
            )
        }

        // 3. Number Plate Text Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.72f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = plateText,
                fontSize = if (config.type == PlateType.BIKE) 10.sp else 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black, // Preview mein standard black rakhein
                maxLines = 1
            )
        }
    }
}