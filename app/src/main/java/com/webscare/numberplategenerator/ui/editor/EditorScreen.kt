package com.webscare.numberplategenerator.ui.editor

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.webscare.numberplategenerator.R
import com.webscare.numberplategenerator.ui.MainViewModel
import com.webscare.numberplategenerator.ui.theme.bg_color
import com.webscare.numberplategenerator.ui.theme.black_color
import com.webscare.numberplategenerator.ui.theme.grey_color
import com.webscare.numberplategenerator.ui.theme.white_color
import com.webscare.numberplategenerator.utils.addPressEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditorScreen (
    viewModel: MainViewModel = koinViewModel()
){
    val selectedTabId by viewModel.selectedTabId.collectAsState()
Column(
    modifier = Modifier
        .fillMaxSize()
        .background(white_color)
) {
    EditorHeader()
    Spacer(modifier = Modifier.height(12.dp))
    PlatePreviewContainer()
    Spacer(modifier = Modifier.height(8.dp))
    EditorTabsBar(
        tabs = viewModel.editorTabs,
        selectedTabId = selectedTabId,
        onTabSelected = { id -> viewModel.onTabSelected(id) }
    )
}
}

@Composable
fun EditorHeader()
{
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 54.dp),
        verticalAlignment = Alignment.CenterVertically,
    )
    {
        Box(
            modifier = Modifier
                .size(40.dp)
                .addPressEffect()
                .background(
                    grey_color.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                )
            ,
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.arrow_back),
                contentDescription = "",
                modifier = Modifier.size(22.dp),
                colorFilter = ColorFilter.tint(black_color))


        }
        Column(
            modifier = Modifier.weight(1f)
                .padding(start = 8.dp, end = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Editor",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = black_color,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )

        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .addPressEffect() { }
                .background(grey_color.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp))
            ,
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_share),
                contentDescription = "",
                modifier = Modifier.size(16.dp),
                colorFilter = ColorFilter.tint(black_color))


        }
        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(40.dp)
                .addPressEffect() { }
                .background(grey_color.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp))
            ,
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_save),
                contentDescription = "",
                modifier = Modifier.size(16.dp),
                colorFilter = ColorFilter.tint(black_color))


        }
    }
}
