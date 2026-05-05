package com.webscare.numberplategenerator.domain.model

import com.webscare.numberplategenerator.ui.PlateType

data class DimensionOption(
    val id: String,
    val label: String,
    val width: Float,
    val height: Float,
    val plateType: PlateType,
    val isFront: Boolean = false
)