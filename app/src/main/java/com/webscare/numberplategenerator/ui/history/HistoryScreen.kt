package com.webscare.numberplategenerator.ui.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplategenerator.R
import com.webscare.numberplategenerator.ui.MainViewModel
import com.webscare.numberplategenerator.ui.components.FilterTabItem
import com.webscare.numberplategenerator.ui.components.GenericFilterRow
import com.webscare.numberplategenerator.ui.components.GenericTitleSection
import com.webscare.numberplategenerator.ui.explore.ExploreHeader
import com.webscare.numberplategenerator.ui.theme.bg_color
import com.webscare.numberplategenerator.ui.theme.black_color
import com.webscare.numberplategenerator.ui.theme.grey_color
import com.webscare.numberplategenerator.ui.theme.red_color
import com.webscare.numberplategenerator.ui.theme.white_color
import com.webscare.numberplategenerator.utils.addPressEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryScreen(
    onBackClick: () -> Unit,
    viewModel: MainViewModel = koinViewModel()
) {
    val recents by viewModel.recents.collectAsState()
    val selectedFilterId by viewModel.selectedFilterId.collectAsState()
    val filterTabs = listOf(
        FilterTabItem("1", "All", R.drawable.ic_grid),
        FilterTabItem("2", "Car", R.drawable.ic_car),
        FilterTabItem("3", "Bike", R.drawable.ic_motorbike)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .background(bg_color)
            .padding(top = 54.dp)
    ) {
        item {
            Column {
                Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                    RecentHeader (onBackClick = onBackClick)
                    GenericTitleSection(
                        title = "MY PLATES",
                        subtitle = "Recents",
                        description = "${recents.size} designs"
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(12.dp))
            GenericFilterRow(
                items = filterTabs,
                selectedId = selectedFilterId,
                onItemSelected = { viewModel.onFilterSelected(it.id) }
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
        items(recents.size) { index ->
            val recent = recents[index]

            HistoryItemRow(
                plateNumber = recent.plateNumber,
                time = "2h ago",
                userName = "Hamza Ali",
                onEditClick = { /* Handle Edit */ },
                onDeleteClick = { /* Handle Delete */ }
            )
        }

        // 4. Bottom spacing
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun RecentHeader(
    onBackClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(white_color)
                .addPressEffect{ onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = "Back",
                modifier = Modifier.size(26.dp),
                colorFilter = ColorFilter.tint(black_color)
            )
        }

    }
}

@Composable
fun HistoryItemRow(
    plateNumber: String,
    time: String,
    userName: String,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .addPressEffect(),
        shape = RoundedCornerShape(16.dp),
        color = white_color,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(0.5f)
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F5F5))
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.number_plate_placeholder),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 2. Column with 3 Texts
            Column(modifier = Modifier.weight(0.35f)) {
                Text(text = plateNumber, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = black_color,
                    lineHeight = 14.sp)
                Text(text = time, fontSize = 12.sp, color = grey_color, lineHeight = 12.sp)
                Text(text = userName, fontSize = 12.sp, color = grey_color,  lineHeight = 12.sp)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.size(32.dp).addPressEffect().clip(CircleShape).background(Color(0xFFF5F5F5))) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_copy),
                        contentDescription = "Copy",
                        modifier = Modifier.padding(6.dp),
                        tint = black_color
                    )                }
                Box(modifier = Modifier.size(32.dp).addPressEffect().clip(CircleShape).background(Color(0xFFF5F5F5))) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = "Delete",
                        modifier = Modifier.padding(6.dp),
                        tint = red_color
                    )                }
            }
        }
    }
}
