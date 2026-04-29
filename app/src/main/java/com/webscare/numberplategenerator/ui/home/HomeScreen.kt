package com.webscare.numberplategenerator.ui.home


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplategenerator.R
import com.webscare.numberplategenerator.domain.model.PlateTemplate
import com.webscare.numberplategenerator.domain.model.RecentPlate
import com.webscare.numberplategenerator.ui.MainViewModel
import com.webscare.numberplategenerator.ui.theme.NumberPlateGeneratorTheme
import com.webscare.numberplategenerator.ui.theme.bg_color
import com.webscare.numberplategenerator.ui.theme.black_color
import com.webscare.numberplategenerator.ui.theme.grey_color
import com.webscare.numberplategenerator.ui.theme.orange_color
import com.webscare.numberplategenerator.ui.theme.pink_color
import com.webscare.numberplategenerator.ui.theme.white_color
import com.webscare.numberplategenerator.utils.addPressEffect
import org.koin.androidx.compose.koinViewModel


@Preview(showSystemUi = true)
@Composable
fun PreviewHomeScreenFull() {
    NumberPlateGeneratorTheme {
        Surface {
            HomeScreen(onNotificationClick = {})
        }
    }
}

@Composable
fun HomeScreen(
    viewModel : MainViewModel = koinViewModel(),
    onNotificationClick: () -> Unit = {},
) {
    val templates by viewModel.templates.collectAsState()
    val recents by viewModel.recents.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(bg_color)
    ) {

        item {
            HomeHeaderSection(onNotificationClick = onNotificationClick)
        }
        item {
            SectionHeader(title = "Browse templates", onSeeAllClick = { /* Handle nav */ })
            TemplateHorizontalList(templates = templates)
        }
        item {
            SectionHeader(title = "Recents", onSeeAllClick = { /* Handle nav */ })
            RecentHorizontalList(recents = recents)
        }

        item { Spacer(modifier = Modifier.height(100.dp)) }

    }
}

@Composable
fun TemplateHorizontalList(
    templates: List<PlateTemplate>
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(templates) { item ->
            TemplateCard(template = item)
        }
    }
}
@Composable
fun TemplateCard(template: PlateTemplate) {
    Card(
        modifier = Modifier
            .width(230.dp) // Screenshot ke mutabiq fixed width
            .padding(vertical = 12.dp)
            .addPressEffect(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Image Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = template.image), // Use your specific drawable
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                Text(
                    text = template.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = template.description,
                    fontSize = 10.sp,
                    lineHeight = 10.sp,
                    color = grey_color
                )
            }
        }
    }

}

@Composable
fun Header(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 14.dp),
        verticalAlignment = Alignment.Top,
    )
    {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    white_color.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "NP",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = white_color,
                textAlign = TextAlign.Start,
            )
        }
        Column(
            modifier = Modifier.weight(1f)
                .padding(start = 8.dp, end = 8.dp),

        ) {
            Text(
                text = "NUMBER PLATE",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = white_color,
                textAlign = TextAlign.Start,
                lineHeight = 10.sp
            )
            Text(
                text = "Maker",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = white_color,
                textAlign = TextAlign.Start,
                lineHeight = 18.sp
            )

        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .addPressEffect() { onClick() }
                .background(white_color.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp))
                ,
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.notification_bell),
                contentDescription = "",
                modifier = Modifier.size(20.dp))

        }
    }
}

@Composable
fun HomeHeaderSection(
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier
)
{
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp),

    ) {
        val gradientBrush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFE91E63),
                Color(0xFFFF6B35),
                Color(0xFF9C27B0)
            ),
            start = Offset(0f, 0f),
            end = Offset.Infinite
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    brush = gradientBrush
                ).padding(top = 40.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(bottom = 40.dp),
            ) {
                // 1. Header (Already exists)
                Header(onClick = onNotificationClick)

                // 2. Main Hero Text (Header ke neeche)
                Column(
                    modifier = Modifier.padding(start = 24.dp, top = 24.dp, end = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Design your perfect plate",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 32.sp
                    )
                    Text(
                        text = "Pakistan-style number plates, customized your way.",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.8f), // Thoda transparent (looks premium)
                        modifier = Modifier.padding(top = 8.dp),
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .addPressEffect()
                            .background(
                                color = white_color,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        val gradientBrush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFE91E63), // rgb(233, 30, 99)
                                Color(0xFF9C27B0)  // rgb(156, 39, 176)
                            ),
                            start = Offset(0f, 0f),       // Top-Left
                            end = Offset.Infinite         // Bottom-Right (135 degree effect ke liye)
                        )

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    brush = gradientBrush,
                                    shape = RoundedCornerShape(12.dp)
                                )
                            ,
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_ai_gen),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp))
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "AI Generation",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = black_color,
                                lineHeight = 18.sp
                            )
                            Text(
                                text = "Pick a photo to auto-detect your plate",
                                fontSize = 14.sp,
                                color = grey_color,
                                lineHeight = 14.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(black_color, shape = RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.arrow_front),
                                contentDescription = "Notification",
                                modifier = Modifier.size(20.dp),
                                colorFilter = ColorFilter.tint(white_color)
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun SectionHeader(
    title: String,
    onSeeAllClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            color = Color.Black
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "See all",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = pink_color,
            modifier = Modifier.addPressEffect { onSeeAllClick() }
        )
    }
}

@Composable
fun RecentPlateCard(recent: RecentPlate) {
    Card(
        modifier = Modifier
            .width(160.dp) // Recent cards thode chote acche lagte hain
            .padding(vertical = 12.dp)
            .addPressEffect(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(white_color),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Image Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = recent.image),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Text Area
            Text(
                text = recent.plateNumber,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                color = Color.Black
            )
            Text(
                text = recent.time,
                fontSize = 10.sp,
                lineHeight = 10.sp,
                color = grey_color
            )
        }
    }
}

@Composable
fun RecentHorizontalList(recents: List<RecentPlate>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(recents) { item ->
            RecentPlateCard(recent = item)
        }
    }
}