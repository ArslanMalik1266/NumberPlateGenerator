package com.webscare.numberplategenerator.domain.model

import android.graphics.Color

data class EditorStates(
    val plateText: String = "LEB-2847",
    val selectedTab: EditorTabType = EditorTabType.TEXT,
    val toolStates: Map<EditorTabType, ToolState> = mapOf(
        EditorTabType.TEXT to ToolState.TextState(),
        EditorTabType.STYLE to ToolState.StyleState()
    )
)

sealed class ToolState {
    data class TextState(
        val selectedFontId: String = "1",
        val fontOptions: List<FontOption> = emptyList()
    ) : ToolState()

    data class StyleState(
        val isBold: Boolean = false,
        val isItalic: Boolean = false,
        val isUnderline: Boolean = false,
        val selectedColor: Int = 0xFF000000.toInt(),
    ) : ToolState()
}