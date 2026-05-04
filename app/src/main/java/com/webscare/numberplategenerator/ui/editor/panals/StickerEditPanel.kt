package com.webscare.numberplategenerator.ui.editor.panals

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplategenerator.data.datasource.AssetDataProvider
import com.webscare.numberplategenerator.domain.model.EditorTabType
import com.webscare.numberplategenerator.ui.MainViewModel
import com.webscare.numberplategenerator.ui.ToolState
import com.webscare.numberplategenerator.ui.theme.grey_color
import com.webscare.numberplategenerator.ui.theme.pink_color
import com.webscare.numberplategenerator.ui.theme.white_color
import com.webscare.numberplategenerator.utils.addPressEffect
import org.koin.compose.viewmodel.koinActivityViewModel

@Composable
fun StickerEditPanel(
    viewModel: MainViewModel = koinActivityViewModel()
) {
    val state by viewModel.editorState.collectAsState()
    val stickerState = state.toolStates[EditorTabType.STICKER] as? ToolState.StickerState
        ?: ToolState.StickerState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) {
        item {
            Text(
                text = "ADD STICKER",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = grey_color
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Box(modifier = Modifier.heightIn(max = 250.dp)) {

                val stickers = AssetDataProvider.stickerOptions
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4), // 4 Columns as per image
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // StickerEditPanel mein
                    items(AssetDataProvider.stickerOptions) { stickerRes ->
                        val isSelected = if (stickerRes == null) {
                            stickerState.selectedStickerId == "none"
                        } else {
                            stickerState.selectedStickerId == stickerRes.toString()
                        }

                        StickerItemCard(
                            resId = stickerRes,
                            isSelected = isSelected,
                            tintColor = androidx.compose.ui.graphics.Color(stickerState.stickerTint),
                            onClick = {
                                // ID ke liye hum resId ka string use kar rahe hain
                                val id = stickerRes?.toString() ?: "none"
                                viewModel.onStickerSelect(id, stickerRes)
                            }
                        )
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "STICKER COLOR",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = grey_color
            )
            Spacer(modifier = Modifier.height(16.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
                AssetDataProvider.solidColors.forEach { colorInt ->
                    val isColorSelected = stickerState.stickerTint == colorInt

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = androidx.compose.ui.graphics.Color(colorInt),
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                            .border(
                                width = if (isColorSelected) 2.dp else 1.dp,
                                color = if (isColorSelected) pink_color else grey_color.copy(alpha = 0.2f),
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                            .addPressEffect {
                                viewModel.onStickerColorSelect(colorInt)
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun StickerItemCard(
    resId: Int?,
    isSelected: Boolean,
    tintColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .aspectRatio(1f) // Perfect Square
            .background(
                color = if (isSelected) pink_color.copy(alpha = 0.08f) else white_color,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.5.dp,
                color = if (isSelected) pink_color else grey_color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .addPressEffect { onClick() }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        if (resId != null) {
            Image(
                painter = painterResource(id = resId),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().padding(8.dp),
                colorFilter = ColorFilter.tint(tintColor)
            )
        } else {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "None",
                tint = grey_color.copy(alpha = 0.4f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}