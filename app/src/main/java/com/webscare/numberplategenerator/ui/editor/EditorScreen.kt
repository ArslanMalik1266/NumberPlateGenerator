package com.webscare.numberplategenerator.ui.editor

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplategenerator.R
import com.webscare.numberplategenerator.domain.model.EditorTabType
import com.webscare.numberplategenerator.ui.MainViewModel
import com.webscare.numberplategenerator.ui.editor.canvas.PlateCanvasPreview
import com.webscare.numberplategenerator.ui.editor.panals.HeaderEditPanel
import com.webscare.numberplategenerator.ui.editor.panals.DimensionEditPanel
import com.webscare.numberplategenerator.ui.editor.panals.FlagEditPanel
import com.webscare.numberplategenerator.ui.editor.panals.NameEditPanel
import com.webscare.numberplategenerator.ui.editor.panals.StickerEditPanel
import com.webscare.numberplategenerator.ui.editor.panals.StyleEditPanel
import com.webscare.numberplategenerator.ui.editor.panals.TextEditPanel
import com.webscare.numberplategenerator.ui.editor.panals.TypeEditPanel
import com.webscare.numberplategenerator.ui.theme.black_color
import com.webscare.numberplategenerator.ui.theme.grey_color
import com.webscare.numberplategenerator.ui.theme.pink_color
import com.webscare.numberplategenerator.ui.theme.white_color
import com.webscare.numberplategenerator.utils.addPressEffect
import org.koin.compose.viewmodel.koinActivityViewModel

@Composable
fun EditorScreen(
    viewModel: MainViewModel = koinActivityViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.editorState.collectAsState()
    var rotationTarget by remember {
        mutableFloatStateOf(if (state.isFront) 0f else 180f)
    }
    val rotationAngle by animateFloatAsState(
        targetValue = rotationTarget,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "PlateFlip"
    )
    LaunchedEffect(rotationAngle) {
        // Agar angle 90 cross kar jaye aur state abhi tak purani hai, to update karein
        if (rotationAngle > 90f && state.isFront) {
            viewModel.updateSide(false) // Back kar do
        } else if (rotationAngle < 90f && !state.isFront) {
            viewModel.updateSide(true) // Front kar do
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetEditorState()
        }
    }
    BackHandler {
        onBack()
        viewModel.resetEditorState()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(white_color)
    ) {
        EditorHeader(
            onBackClick = onBack
        )
        Spacer(modifier = Modifier.height(12.dp))
        PlateSideToggle(
            isFront = state.isFront,
            onSideSelected = { isFrontClick ->
                rotationTarget = if (isFrontClick) 0f else 180f
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        PlateCanvasPreview(
            state = state,
            externalRotation = rotationAngle,
            onLongPressAction = { isPressed ->
                viewModel.onHeaderLongPress(isPressed)
            },
            onHeaderMove = { dragAmount ->
                viewModel.onHeaderMove(dragAmount)
            },
            onHeaderReset = {
                viewModel.resetHeaderOffset()
            },
            onHeaderDropped = { finalTouchOffset, plateSize ->
                viewModel.onHeaderDropped(finalTouchOffset, plateSize)
            }
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
                EditorTabType.DIMENSION -> {
                    DimensionEditPanel(viewModel = viewModel)
                }

                EditorTabType.TEXT -> {
                    TextEditPanel(viewModel = viewModel)
                }

                EditorTabType.STYLE -> {
                    StyleEditPanel(viewModel = viewModel)
                }

                EditorTabType.HEADER -> {
                    HeaderEditPanel(viewModel = viewModel)
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

@Composable
fun PlateSideToggle(
    isFront: Boolean,
    onSideSelected: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            // Front Option
            ToggleOption(
                text = "Front",
                isSelected = isFront,
                onClick = { onSideSelected(true) }
            )
            // Back Option
            ToggleOption(
                text = "Back",
                isSelected = !isFront,
                onClick = { onSideSelected(false) }
            )
        }
    }
}

@Composable
fun ToggleOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .addPressEffect { onClick() }
            .background(
                color = if (isSelected) white_color else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 4.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) pink_color else grey_color
        )
    }
}
