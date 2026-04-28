package com.webscare.numberplategenerator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.webscare.numberplategenerator.R
import com.webscare.numberplategenerator.ui.navigation.BottomNavItem
import com.webscare.numberplategenerator.ui.navigation.Screen
import com.webscare.numberplategenerator.ui.theme.grey_color
import com.webscare.numberplategenerator.ui.theme.pink_color
import com.webscare.numberplategenerator.ui.theme.white_color



@Composable
fun FloatingBottomBar(currentRoute: String?,
                      items: List<BottomNavItem>,
                      onNavigate: (String) -> Unit) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        color = white_color.copy(alpha = 0.9f),
        shape = RoundedCornerShape(40.dp),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(vertical = 12.dp).background(Color.Transparent),
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
                        // Active route check logic
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