package com.webscare.numberplategenerator.ui.editor.panals

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplategenerator.domain.model.EditorTabType
import com.webscare.numberplategenerator.ui.MainViewModel
import com.webscare.numberplategenerator.ui.ToolState
import com.webscare.numberplategenerator.ui.theme.black_color
import com.webscare.numberplategenerator.ui.theme.grey_color
import com.webscare.numberplategenerator.ui.theme.white_color
import org.koin.compose.viewmodel.koinActivityViewModel

@Composable
fun NameEditPanel(
    viewModel: MainViewModel = koinActivityViewModel()
) {
    val state by viewModel.editorState.collectAsState()

    // Map se state nikalain
    val nameState = state.toolStates[EditorTabType.NAME] as? ToolState.NameState
        ?: ToolState.NameState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) {
        Text(
            text = "OWNER / NAME (OPTIONAL)",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = grey_color
        )

        Spacer(modifier = Modifier.height(12.dp))

        // --- Custom Styled TextField ---
        OutlinedTextField(
            value = nameState.ownerName,
            onValueChange = { viewModel.onOwnerNameChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Name...", color = grey_color.copy(alpha = 0.4f)) },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = grey_color.copy(alpha = 0.2f),
                unfocusedBorderColor = grey_color.copy(alpha = 0.2f),
                focusedContainerColor = white_color,
                unfocusedContainerColor = white_color
            ),
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = black_color
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Info / Alert Box ---
        InfoCard(
            message = "Adding a name is optional and shows below the plate number — useful for personalized display plates only."
        )
    }
}

@Composable
fun InfoCard(message: String) {
    val orangeMain = Color(0xFFF57C00)
    val orangeBg = Color(0xFFFFF3E0)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(orangeBg, RoundedCornerShape(16.dp))
            .border(1.dp, orangeMain.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            // Material design ka info icon ya apna drawable
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = orangeMain,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = message,
            fontSize = 14.sp,
            color = Color.DarkGray.copy(alpha = 0.8f),
            lineHeight = 20.sp,
            fontWeight = FontWeight.Normal
        )
    }
}