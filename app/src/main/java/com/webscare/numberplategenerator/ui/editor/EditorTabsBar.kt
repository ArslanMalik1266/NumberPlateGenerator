package com.webscare.numberplategenerator.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplategenerator.R
import com.webscare.numberplategenerator.domain.model.EditorTabType
import com.webscare.numberplategenerator.ui.theme.pink_color

@Composable
fun EditorTabsBar(
    tabs: List<EditorTab>,
    selectedTabId: EditorTabType,
    onTabSelected: (EditorTabType) -> Unit
) {


    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(tabs) { tab ->
            val isSelected = selectedTabId == tab.id

            Row(
                modifier = Modifier.height(32.dp)
                    .clip(RoundedCornerShape(40))
                    .background(if (isSelected) pink_color else Color.Transparent)
                    .clickable {  onTabSelected(tab.id) }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = tab.iconRes),
                    contentDescription = tab.label,
                    tint = if (isSelected) Color.White else Color.Gray,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = tab.label,
                    color = if (isSelected) Color.White else Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}