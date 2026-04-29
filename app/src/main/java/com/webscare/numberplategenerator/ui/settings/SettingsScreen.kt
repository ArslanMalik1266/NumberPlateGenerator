package com.webscare.numberplategenerator.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplategenerator.R
import com.webscare.numberplategenerator.ui.history.RecentHeader
import com.webscare.numberplategenerator.ui.theme.bg_color
import com.webscare.numberplategenerator.ui.theme.black_color
import com.webscare.numberplategenerator.ui.theme.grey_color
import com.webscare.numberplategenerator.ui.theme.pink_color
import com.webscare.numberplategenerator.ui.theme.white_color
import com.webscare.numberplategenerator.utils.addPressEffect

@Composable
fun SettingsScreen() {
    var isDarkMode by remember { mutableStateOf(false) }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .background(bg_color)
            .padding(top = 54.dp)
    ) {
        item {
            settingsHeader(
                subtitle = "Settings"
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            ProfileCard(
                name = "Hamza Ali",
                platesCount = 4,
                onClick = { /* Handle navigation */ }
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            SettingsText(text = "APPEARANCE")
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            SettingsGroupCard {
                SettingsListItem(
                    iconRes = R.drawable.ic_light_theme,
                    text = "Light / Dark",
                    onClick = { },
                    showDivider = false,
                    trailingContent = {
                        ThemeToggle(
                            isDarkMode = isDarkMode,
                            onToggle = { isDarkMode = it }
                        )
                    }
                )

            }
        }
        item {
            SettingsText(text = "ABOUT")
            Spacer(modifier = Modifier.height(12.dp))
        }
        item {
            SettingsGroupCard {
                SettingsListItem(
                    iconRes = R.drawable.ic_version,
                    text = "Version",
                    onClick = { },
                    trailingContent = {

                    }
                )
                SettingsListItem(
                    iconRes = R.drawable.ic_rate_app,
                    text = "Rate this app",
                    onClick = { },
                    trailingContent = {

                    }
                )
                SettingsListItem(
                    iconRes = R.drawable.ic_privacy,
                    text = "Privacy policy",
                    onClick = { },
                    showDivider = false,
                    trailingContent = {

                    }
                )

            }
        }


    }
}

@Composable
fun settingsHeader(
    subtitle: String = ""
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (subtitle.isNotEmpty()) {
            Text(
                text = subtitle,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = black_color,
                lineHeight = 32.sp
            )
        }
    }
}

@Composable
fun ProfileCard(
    name: String,
    platesCount: Int,
    onClick: () -> Unit
) {
    val gradient = Brush.horizontalGradient(
        colors = listOf(pink_color, Color(0xFFFFC371))
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(vertical = 8.dp)
            .addPressEffect { onClick() },
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .background(gradient)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Circular Avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(1.dp, white_color, CircleShape)
                    .clip(CircleShape)
                    .background(white_color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "HA",
                    color = white_color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))


            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    color = white_color,
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$platesCount plates",
                    color = white_color.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                )
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(white_color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_front),
                    contentDescription = "profile",
                    modifier = Modifier.size(26.dp),
                    colorFilter = ColorFilter.tint(black_color)
                )
            }
        }
    }
}

@Composable
fun SettingsText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = grey_color
) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = modifier
    )
}

@Composable
fun SettingsGroupCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        color = white_color,
    ) {
        Column(
            content = content
        )
    }
}


// Component 2: Single List Item Row
@Composable
fun SettingsListItem(
    iconRes: Int,
    text: String,
    modifier: Modifier = Modifier,
    showDivider: Boolean = true,
    onClick: () -> Unit = {},
    trailingContent: @Composable () -> Unit = {}
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(pink_color.copy(alpha = 0.1f)), // Faded Pink background
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    colorFilter = ColorFilter.tint(pink_color)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = text,
                color = black_color,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )
            Box {
                trailingContent()
            }
        }

        // Horizontal Divider
        if (showDivider) {
            Divider(
                color = Color(0xFFEEEEEE),
                thickness = 1.dp,
            )
        }
    }
}

@Composable
fun ThemeToggle(
    isDarkMode: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)) // Gray background
            .padding(2.dp)
    ) {
        // Light Option
        ToggleOption(
            text = "Light",
            iconRes = R.drawable.ic_light_theme,
            isSelected = !isDarkMode,
            onClick = { onToggle(false) }
        )
        // Dark Option
        ToggleOption(
            text = "Dark",
            iconRes = R.drawable.ic_dark_theme,
            isSelected = isDarkMode,
            onClick = { onToggle(true) }
        )
    }
}

@Composable
fun ToggleOption(
    text: String,
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) white_color else Color.Transparent)
            .addPressEffect { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(10.dp),
            tint = if (isSelected) pink_color else grey_color
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) pink_color else grey_color
        )
    }
}