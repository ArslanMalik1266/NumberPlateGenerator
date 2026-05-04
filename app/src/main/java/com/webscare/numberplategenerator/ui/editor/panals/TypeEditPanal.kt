package com.webscare.numberplategenerator.ui.editor.panals

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplategenerator.R
import com.webscare.numberplategenerator.ui.MainViewModel
import com.webscare.numberplategenerator.ui.PlateType
import com.webscare.numberplategenerator.ui.theme.black_color
import com.webscare.numberplategenerator.ui.theme.grey_color
import com.webscare.numberplategenerator.ui.theme.pink_color
import com.webscare.numberplategenerator.ui.theme.white_color
import com.webscare.numberplategenerator.utils.addPressEffect
import org.koin.compose.viewmodel.koinActivityViewModel

@Composable
fun TypeEditPanel(
    viewModel: MainViewModel = koinActivityViewModel()
) {
    val state by viewModel.editorState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) {
        Text(
            text = "PLATE TYPE",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = grey_color
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TypeCard(
                title = "Car",
                subtitle = "Four-wheeler",
                iconRes = R.drawable.ic_car, // Apna car icon use karein
                isSelected = state.plateType == PlateType.CAR,
                modifier = Modifier.weight(1f),
                onClick = { viewModel.updatePlateType(PlateType.CAR) }
            )

            TypeCard(
                title = "Bike",
                subtitle = "Two-wheeler",
                iconRes = R.drawable.ic_motorbike, // Apna bike icon use karein
                isSelected = state.plateType == PlateType.BIKE,
                modifier = Modifier.weight(1f),
                onClick = { viewModel.updatePlateType(PlateType.BIKE) }
            )
        }
    }
}

@Composable
fun TypeCard(
    title: String,
    subtitle: String,
    iconRes: Int,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Column(
        modifier = modifier
            .background(
                color = if (isSelected) pink_color.copy(alpha = 0.05f) else white_color,
                shape = RoundedCornerShape(24.dp)
            )
            .border(
                width = 2.dp,
                color = if (isSelected) pink_color else grey_color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(24.dp)
            )
            .addPressEffect { onClick() }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    color = pink_color.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                tint = pink_color,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = black_color
        )

        Text(
            text = subtitle,
            fontSize = 14.sp,
            color = grey_color
        )
    }
}