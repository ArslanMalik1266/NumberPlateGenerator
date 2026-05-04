package com.webscare.numberplategenerator.ui.editor.panals

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplategenerator.data.datasource.AssetDataProvider
import com.webscare.numberplategenerator.domain.model.EditorTabType
import com.webscare.numberplategenerator.ui.FlagOption
import com.webscare.numberplategenerator.ui.MainViewModel
import com.webscare.numberplategenerator.ui.ToolState
import com.webscare.numberplategenerator.ui.theme.grey_color
import com.webscare.numberplategenerator.ui.theme.pink_color
import com.webscare.numberplategenerator.utils.addPressEffect
import org.koin.compose.viewmodel.koinActivityViewModel

@Composable
fun FlagEditPanel(
    viewModel: MainViewModel = koinActivityViewModel()
) {
    val state by viewModel.editorState.collectAsState()
    val flagState = state.toolStates[EditorTabType.FLAG] as? ToolState.FlagState
        ?: ToolState.FlagState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) {
        Text(
            text = "ADD FLAG / EMBLEM",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = grey_color
        )

        Spacer(modifier = Modifier.height(16.dp))
        val flags = AssetDataProvider.flagOptions
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(flags) { flag ->
                FlagItemCard(
                    flag = flag,
                    isSelected = flag.id == flagState.selectedFlagId,
                    onClick = {
                        // Ab hum id aur resId dono bhej rahe hain
                        viewModel.onFlagSelect(flag.id, flag.resId)
                    }
                )
            }
        }
    }
}

@Composable
fun FlagItemCard(
    flag: FlagOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isSelected) pink_color.copy(alpha = 0.05f) else Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.5.dp,
                color = if (isSelected) pink_color else grey_color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            )
            .addPressEffect{ onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Flag Image or "None" Icon
        Box(
            modifier = Modifier
                .size(width = 60.dp, height = 40.dp)
                .background(
                    color = if (flag.resId == null) Color.Transparent else Color.Transparent,
                    shape = RoundedCornerShape(4.dp)
                )
            ,
            contentAlignment = Alignment.Center
        ) {
            if (flag.resId != null) {
                Image(
                    painter = painterResource(id = flag.resId),
                    contentDescription = flag.name,
                    modifier = Modifier.fillMaxSize().padding(8.dp)
                )
            } else {
                // "None" cross icon
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "None",
                    tint = grey_color.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp).border(1.dp, grey_color.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = flag.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}