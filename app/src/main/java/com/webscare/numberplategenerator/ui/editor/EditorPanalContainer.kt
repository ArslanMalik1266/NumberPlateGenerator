package com.webscare.numberplategenerator.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.webscare.numberplategenerator.ui.theme.bg_color

@Composable
fun EditorPanelContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = bg_color,
            )
            .padding(top = 16.dp, start = 24.dp, end = 24.dp, bottom = 16.dp)
    ) {
        content()
    }
}