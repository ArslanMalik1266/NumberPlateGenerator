package com.webscare.numberplategenerator.ui

import com.webscare.numberplategenerator.domain.model.EditorTabType
import com.webscare.numberplategenerator.domain.model.FontOption

data class EditorStates(
    val plateType: PlateType = PlateType.CAR,
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
        val fontOptions: List<FontOption> = emptyList(),
        val selectedFont: FontOption? = null
    ) : ToolState()

    data class StyleState(
        val isBold: Boolean = false,
        val isItalic: Boolean = false,
        val isUnderline: Boolean = false,
        val selectedColor: Int = 0xFF000000.toInt(),
    ) : ToolState()
}

enum class PlateType {
    CAR, BIKE
}