package com.webscare.numberplategenerator.ui.editor.panals

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplategenerator.data.datasource.AssetDataProvider
import com.webscare.numberplategenerator.domain.model.DimensionOption
import com.webscare.numberplategenerator.domain.model.EditorTabType
import com.webscare.numberplategenerator.ui.MainViewModel
import com.webscare.numberplategenerator.ui.PlateType
import com.webscare.numberplategenerator.ui.ToolState
import com.webscare.numberplategenerator.ui.editor.canvas.PlateDimensionThumbnail
import com.webscare.numberplategenerator.ui.theme.black_color
import com.webscare.numberplategenerator.ui.theme.grey_color
import com.webscare.numberplategenerator.ui.theme.pink_color
import com.webscare.numberplategenerator.ui.theme.white_color
import com.webscare.numberplategenerator.utils.addPressEffect
import org.koin.compose.viewmodel.koinActivityViewModel

@Composable
fun DimensionEditPanel(
    viewModel: MainViewModel = koinActivityViewModel(),
) {
    val state by viewModel.editorState.collectAsState()
    val dimState = state.toolStates[EditorTabType.DIMENSION] as? ToolState.DimensionState
    val currentSelectedId = when (state.plateType) {
        PlateType.CAR -> if (state.isFront) dimState?.carFrontId else dimState?.carBackId
        PlateType.BIKE -> if (state.isFront) dimState?.bikeFrontId else dimState?.bikeBackId}

    val bgState = state.toolStates[EditorTabType.HEADER] as? ToolState.BackgroundState
    val selectedBg = AssetDataProvider.plateBackgrounds
        .find { it.id == bgState?.selectedBackgroundId }
        ?: AssetDataProvider.plateBackgrounds.first()
    val filteredDimensions = AssetDataProvider.plateDimensions.filter {
        it.plateType == state.plateType && it.isFront == state.isFront
    }

    Column(modifier = Modifier.padding(top = 10.dp)) {
        Text(
            text = "SELECT ${state.plateType.name} DIMENSION",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = grey_color,
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items = filteredDimensions,
                key = { it.id }) { option ->
                val isSelected = currentSelectedId == option.id

                DimensionItem(
                    option = option,
                    headerColor = selectedBg.headerColor,
                    isSelected = isSelected,
                    onSelect = { viewModel.updatePlateDimension(option) }
                )
            }
        }
    }
}

@Composable
private fun DimensionItem(
    option: DimensionOption,
    headerColor: androidx.compose.ui.graphics.Color,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.9f)
            .addPressEffect { onSelect() },
        // Selection highlight: Border aur Elevation
        border = if (isSelected) BorderStroke(2.dp, pink_color) else null,
        colors = CardDefaults.cardColors(containerColor = white_color)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Preview Section (50%)
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                PlateDimensionThumbnail(
                    width = option.width,
                    height = option.height,
                    headerColor = headerColor,
                    plateColor = white_color,
                    modifier = Modifier.fillMaxWidth(0.85f)
                )
            }

            // Info Section (50%)
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth().padding(top = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = option.label,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    color = if (isSelected) pink_color else black_color,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "${option.width.toInt()}x${option.height.toInt()} mm",
                    fontSize = 10.sp,
                    lineHeight = 10.sp,
                    color = grey_color
                )
            }
        }
    }
}