package com.webscare.numberplategenerator.ui

import android.graphics.Color
import com.webscare.numberplategenerator.R
import com.webscare.numberplategenerator.domain.model.EditorTabType
import com.webscare.numberplategenerator.domain.model.FontOption

data class EditorStates(
    val plateType: PlateType = PlateType.CAR,
    val plateText: String = "LEB-2847",
    val isFront: Boolean = true,
    val selectedTab: EditorTabType = EditorTabType.DIMENSION,
    val toolStates: Map<EditorTabType, ToolState> = mapOf(
        EditorTabType.DIMENSION to ToolState.DimensionState(),
        EditorTabType.TEXT to ToolState.TextState(),
        EditorTabType.STYLE to ToolState.StyleState(),
        EditorTabType.NAME to ToolState.NameState(),
        EditorTabType.FLAG to ToolState.FlagState(),
        EditorTabType.STICKER to ToolState.StickerState(),
        EditorTabType.BACKGROUND to ToolState.BackgroundState()
    )
)

data class FlagOption(
    val id: String,
    val name: String,
    val resId: Int? = null
)

sealed class ToolState {
    data class DimensionState(
        // Charo scenarios ke liye alag slots
        val carBackId: String = "car_back_std",
        val bikeBackId: String = "bike_back_std"
    ) : ToolState()
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

    data class NameState(
        val ownerName: String = "",
        val isVisible: Boolean = true
    ) : ToolState()

    data class FlagState(
        val selectedFlagId: String = "none",
        val selectedFlagRes: Int? = null
    ) : ToolState()

    data class StickerState(
        val selectedStickerId: String = "none",
        val selectedStickerRes: Int? = null,
        val stickerTint: Int = 0xFF000000.toInt()

        ) : ToolState()

    data class BackgroundState(
        val selectedBackgroundId: String = "pb_car"
    ) : ToolState()
}

enum class PlateType {
    CAR, BIKE
}

data class PlateBackground(
    val id: String,
    val name: String,
    val description: String,
    val headerColor: androidx.compose.ui.graphics.Color,
    val plateColor: androidx.compose.ui.graphics.Color,
    val headerText: String,
    val type: PlateType
)