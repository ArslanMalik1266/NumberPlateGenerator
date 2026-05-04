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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplategenerator.R
import com.webscare.numberplategenerator.domain.model.EditorTabType
import com.webscare.numberplategenerator.ui.MainViewModel
import com.webscare.numberplategenerator.ui.editor.panals.BackgroundEditPanel
import com.webscare.numberplategenerator.ui.editor.panals.FlagEditPanel
import com.webscare.numberplategenerator.ui.editor.panals.NameEditPanel
import com.webscare.numberplategenerator.ui.editor.panals.StickerEditPanel
import com.webscare.numberplategenerator.ui.editor.panals.StyleEditPanel
import com.webscare.numberplategenerator.ui.editor.panals.TextEditPanel
import com.webscare.numberplategenerator.ui.editor.panals.TypeEditPanel
import com.webscare.numberplategenerator.ui.theme.black_color
import com.webscare.numberplategenerator.ui.theme.grey_color
import com.webscare.numberplategenerator.ui.theme.white_color
import com.webscare.numberplategenerator.utils.addPressEffect
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.viewmodel.koinActivityViewModel

@Composable
fun EditorScreen(
    viewModel: MainViewModel = koinActivityViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.editorState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(white_color)
    ) {
        EditorHeader(
            onBackClick = onBack
        )
        Spacer(modifier = Modifier.height(12.dp))
        PlatePreviewContainer(
            state = state
        )
        Spacer(modifier = Modifier.height(8.dp))
        EditorTabsBar(
            tabs = viewModel.editorTabs,
            selectedTabId = state.selectedTab,
            onTabSelected = { id -> viewModel.onTabSelected(id) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        EditorPanelContainer(modifier = Modifier.weight(1f)) {
            when (state.selectedTab) {
                EditorTabType.TEXT -> {
                    TextEditPanel(viewModel = viewModel)
                }

                EditorTabType.STYLE -> {
                    StyleEditPanel(viewModel = viewModel)
                }

                EditorTabType.BACKGROUND -> {
                    BackgroundEditPanel(viewModel = viewModel)
                }

                EditorTabType.FLAG -> {
                    FlagEditPanel(viewModel = viewModel)
                }

                EditorTabType.TYPE -> {
                    TypeEditPanel(viewModel = viewModel)
                }

                EditorTabType.NAME -> {
                    NameEditPanel(viewModel = viewModel)
                }
                EditorTabType.STICKER -> {
                    StickerEditPanel(viewModel = viewModel)
                }

                else -> { /* Default case */
                }
            }
        }
    }
}

@Composable
fun EditorHeader(onBackClick: () -> Unit) {
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
                .addPressEffect {
                    onBackClick()
                }
                .background(
                    grey_color.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.arrow_back),
                contentDescription = "",
                modifier = Modifier.size(22.dp),
                colorFilter = ColorFilter.tint(black_color)
            )


        }
        Column(
            modifier = Modifier
                .weight(1f)
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
                .background(
                    grey_color.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_share),
                contentDescription = "",
                modifier = Modifier.size(16.dp),
                colorFilter = ColorFilter.tint(black_color)
            )


        }
        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(40.dp)
                .addPressEffect() { }
                .background(
                    grey_color.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_save),
                contentDescription = "",
                modifier = Modifier.size(16.dp),
                colorFilter = ColorFilter.tint(black_color)
            )


        }
    }
}
