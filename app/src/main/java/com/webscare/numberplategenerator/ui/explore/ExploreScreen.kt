package com.webscare.numberplategenerator.ui.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplategenerator.R
import com.webscare.numberplategenerator.ui.MainViewModel
import com.webscare.numberplategenerator.ui.components.FilterTabItem
import com.webscare.numberplategenerator.ui.components.GenericFilterRow
import com.webscare.numberplategenerator.ui.components.GenericTitleSection
import com.webscare.numberplategenerator.ui.components.TemplateCard
import com.webscare.numberplategenerator.ui.theme.bg_color
import com.webscare.numberplategenerator.ui.theme.black_color
import com.webscare.numberplategenerator.ui.theme.grey_color
import com.webscare.numberplategenerator.ui.theme.white_color
import com.webscare.numberplategenerator.utils.addPressEffect
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.viewmodel.koinActivityViewModel

@Composable
fun ExploreScreen(
    onBackClick: () -> Unit,
    viewModel: MainViewModel = koinActivityViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val filterTabs = listOf(
        FilterTabItem("1", "All", R.drawable.ic_grid),
        FilterTabItem("2", "Car", R.drawable.ic_car),
        FilterTabItem("3", "Bike", R.drawable.ic_motorbike)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg_color)
            .padding(top = 54.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            ExploreHeader(onBackClick = onBackClick)
            GenericTitleSection(
                title = "GALLERY",
                subtitle = "Browse templates",
                description = "${uiState.templates.size} designs"
            )

            Spacer(modifier = Modifier.height(12.dp))
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = { viewModel.onSearchQueryChange(it) }
            )

            Spacer(modifier = Modifier.height(12.dp))
            GenericFilterRow(
                items = filterTabs,
                selectedId = uiState.selectedFilterId,
                onItemSelected = { viewModel.onFilterSelected(it.id) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            items(uiState.templates) { template ->
                TemplateCard(template = template)
            }

            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun ExploreHeader(
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
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(white_color)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = grey_color,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(fontSize = 14.sp, color = black_color),
            singleLine = true,
            cursorBrush = SolidColor(black_color),
            decorationBox = { innerTextField ->
                if (query.isEmpty()) {
                    Text(
                        text = "Search templates...",
                        color = grey_color,
                        fontSize = 14.sp
                    )
                }
                innerTextField()
            }
        )
    }
}