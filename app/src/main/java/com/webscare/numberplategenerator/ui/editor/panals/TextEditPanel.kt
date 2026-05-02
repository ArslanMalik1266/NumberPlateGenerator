package com.webscare.numberplategenerator.ui.editor.panals

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplategenerator.domain.model.EditorTabType
import com.webscare.numberplategenerator.ui.ToolState
import com.webscare.numberplategenerator.ui.MainViewModel
import com.webscare.numberplategenerator.ui.components.FontCard
import com.webscare.numberplategenerator.ui.theme.black_color
import com.webscare.numberplategenerator.ui.theme.grey_color
import com.webscare.numberplategenerator.ui.theme.white_color
import org.koin.androidx.compose.koinViewModel

@Composable
fun TextEditPanel(
    viewModel: MainViewModel = koinViewModel()
) {
    val state by viewModel.editorState.collectAsState()
    val textState = state.toolStates[EditorTabType.TEXT] as? ToolState.TextState
    val fonts = textState?.fontOptions ?: emptyList()
    val selectedId = textState?.selectedFontId ?: "1"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()

    ) {
        item {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                Text(
                    text = "PLATE NUMBER",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = grey_color
                )
                Spacer(modifier = Modifier.height(8.dp))

                BasicTextField(
                    value = state.plateText,
                    onValueChange = { viewModel.onTextChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(white_color, RoundedCornerShape(16.dp))
                        .border(1.dp, grey_color.copy(0.15f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = black_color
                    ),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            innerTextField()
                        }
                    }
                )


            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // --- 2. Select Font Header ---
        item {
            Text(
                text = "SELECT FONT",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = grey_color
            )
            Spacer(modifier = Modifier.height(8.dp))
        }


        val rows = fonts.chunked(2)
        items(rows) { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { font ->
                    FontCard(
                        font = if (font.id == "6") font.copy(name = "Urdu Font") else font,
                        isSelected = font.id == selectedId,
                        modifier = Modifier.weight(1f),
                        onSelect = { viewModel.onFontSelect(font.id) }
                    )
                }
                if (row.size < 2) Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}