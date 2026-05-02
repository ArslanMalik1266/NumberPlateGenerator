package com.webscare.numberplategenerator.ui

import com.webscare.numberplategenerator.domain.model.PlateTemplate
import com.webscare.numberplategenerator.domain.model.RecentPlate

data class MainState(
    val templates: List<PlateTemplate> = emptyList(),
    val recents: List<RecentPlate> = emptyList(),
    val searchQuery: String = "",
    val selectedFilterId: String = "1",
    val isFabExpanded: Boolean = false
)
