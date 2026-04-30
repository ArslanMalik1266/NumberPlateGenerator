package com.webscare.numberplategenerator.ui.editor

import com.webscare.numberplategenerator.domain.model.EditorTabType

data class EditorTab(
    val id: EditorTabType,
    val label: String,
    val iconRes: Int
)
