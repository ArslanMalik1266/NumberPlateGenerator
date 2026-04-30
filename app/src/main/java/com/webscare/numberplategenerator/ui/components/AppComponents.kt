package com.webscare.numberplategenerator.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplategenerator.R
import com.webscare.numberplategenerator.domain.model.FontOption
import com.webscare.numberplategenerator.domain.model.PlateTemplate
import com.webscare.numberplategenerator.ui.navigation.BottomNavItem
import com.webscare.numberplategenerator.ui.navigation.Screen
import com.webscare.numberplategenerator.ui.theme.black_color
import com.webscare.numberplategenerator.ui.theme.grey_color
import com.webscare.numberplategenerator.ui.theme.pink_color
import com.webscare.numberplategenerator.ui.theme.white_color
import com.webscare.numberplategenerator.utils.addPressEffect


@Composable
fun FloatingBottomBar(
    currentRoute: String?,
    items: List<BottomNavItem>,
    onNavigate: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White.copy(0.4f), Color.White.copy(0.1f))
                ),
                shape = RoundedCornerShape(40.dp)
            ),
        color = white_color.copy(alpha = 0.6f),
        shape = RoundedCornerShape(40.dp),
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .background(Color.Transparent),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,

            ) {
            items.forEachIndexed { index, item ->
                // FAB ke liye beech mein space
                if (index == 2) {
                    Spacer(modifier = Modifier.width(48.dp))
                }

                IconButton(onClick = { onNavigate(item.route) }) {
                    Icon(
                        painter = painterResource(id = item.iconResId),
                        contentDescription = item.label,
                        tint = if (currentRoute == item.route) pink_color else grey_color
                    )
                }
            }
        }
    }
}

@Composable
fun AppFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
        containerColor = Color.Red,
        modifier = modifier.border(
            width = 2.dp,
            color = Color.White,
            shape = CircleShape
        )
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            tint = Color.White
        )
    }
}

@Composable
fun GenericTitleSection(
    title: String,
    subtitle: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = grey_color,
            lineHeight = 14.sp
        )
        Text(
            text = subtitle,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = black_color,
            lineHeight = 32.sp
        )
        Text(
            text = description,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = grey_color,
            lineHeight = 14.sp
        )
    }
}

@Composable
fun CustomFilterChip(
    text: String,
    icon: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) pink_color else Color.White
    val contentColor = if (isSelected) Color.White else Color.Black

    Surface(
        onClick = onClick,
        modifier = modifier
            .height(32.dp)
            .addPressEffect { onClick() },
        shape = RoundedCornerShape(40),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = contentColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun FontCard(
    font: FontOption,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onSelect: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) pink_color.copy(0.05f) else white_color)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) pink_color else grey_color.copy(0.15f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onSelect() }
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = font.name,
            fontSize = 10.sp,
            color = if (isSelected) black_color.copy(0.6f) else grey_color,
            fontWeight = FontWeight.Medium,
            lineHeight = 10.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Image(
            painter = painterResource(font.previewImage),
            contentDescription = "",
            modifier = Modifier
                .padding(8.dp)
        )
    }
}

@Composable
fun GenericFilterRow(
    items: List<FilterTabItem>,
    selectedId: String,
    onItemSelected: (FilterTabItem) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            val isSelected = item.id == selectedId
            CustomFilterChip(
                text = item.title,
                icon = item.icon,
                isSelected = isSelected,
                onClick = { onItemSelected(item) }
            )
        }
    }
}

@Composable
fun TemplateCard(
    template: PlateTemplate,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .addPressEffect(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = white_color),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = template.image),
                    contentDescription = template.title,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Text Area
            Text(
                text = template.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = black_color,
                lineHeight = 14.sp
            )
            Text(
                text = template.description,
                fontSize = 10.sp,
                color = grey_color,
                lineHeight = 10.sp
            )
        }
    }
}

@Composable
fun TemplateGrid(templates: List<PlateTemplate>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2 items in 1 row
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(templates) { item ->
            TemplateCard(template = item)
        }
    }
}