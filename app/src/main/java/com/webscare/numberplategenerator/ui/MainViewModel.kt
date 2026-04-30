package com.webscare.numberplategenerator.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.webscare.numberplategenerator.R
import com.webscare.numberplategenerator.data.datasource.StyleDataProvider
import com.webscare.numberplategenerator.domain.model.EditorStates
import com.webscare.numberplategenerator.domain.model.EditorTabType
import com.webscare.numberplategenerator.domain.model.FontOption
import com.webscare.numberplategenerator.domain.model.PlateTemplate
import com.webscare.numberplategenerator.domain.model.RecentPlate
import com.webscare.numberplategenerator.domain.model.ToolState
import com.webscare.numberplategenerator.domain.usecase.GetRecentPlatesUseCase
import com.webscare.numberplategenerator.domain.usecase.GetTemplatesUseCase
import com.webscare.numberplategenerator.ui.editor.EditorTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val getTemplatesUseCase: GetTemplatesUseCase,
    private val getRecentPlatesUseCase: GetRecentPlatesUseCase
) : ViewModel() {
    private val _templates = MutableStateFlow<List<PlateTemplate>>(emptyList())
    val templates = _templates.asStateFlow()
    private val _recents = MutableStateFlow<List<RecentPlate>>(emptyList())
    val recents = _recents.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    private val _selectedFilterId = MutableStateFlow("1")
    val selectedFilterId = _selectedFilterId.asStateFlow()

    private val _uiState = MutableStateFlow(EditorStates())
    val uiState = _uiState.asStateFlow()
    init {
        loadTemplates()
        loadRecents()
        loadFonts()
    }
    fun onColorSelect(colorInt: Int) {
        _uiState.update { currentState ->
            val currentStyle = (currentState.toolStates[EditorTabType.STYLE] as? ToolState.StyleState)
                ?: ToolState.StyleState()

            val updatedStyle = currentStyle.copy(selectedColor = colorInt)

            currentState.copy(
                toolStates = currentState.toolStates + (EditorTabType.STYLE to updatedStyle)
            )
        }
    }
    private fun loadFonts() {
        val fonts = listOf(
            FontOption("1", "Plate Standard", R.drawable.number_plate_placeholder),
            FontOption("2", "Oswald", R.drawable.number_plate_placeholder),
            FontOption("3", "DM Mono", R.drawable.number_plate_placeholder),
            FontOption("4", "Jakarta", R.drawable.number_plate_placeholder),
            FontOption("5", "Space Grotesk", R.drawable.number_plate_placeholder),
            FontOption("6", "Nastaliq اردو", R.drawable.number_plate_placeholder),
            FontOption("7", "Plate Standard", R.drawable.number_plate_placeholder),
            FontOption("8", "Oswald", R.drawable.number_plate_placeholder),
            FontOption("9", "DM Mono", R.drawable.number_plate_placeholder)
        )
        _uiState.update { currentState ->
            val textState = (currentState.toolStates[EditorTabType.TEXT] as? ToolState.TextState)
                ?.copy(fontOptions = fonts) ?: ToolState.TextState(fontOptions = fonts)

            currentState.copy(
                toolStates = currentState.toolStates + (EditorTabType.TEXT to textState)
            )
        }
    }
    fun onTextChange(newText: String) {
        _uiState.update { it.copy(plateText = newText) }
    }
    fun onFontSelect(id: String) {
        _uiState.update { currentState ->
            val textState = (currentState.toolStates[EditorTabType.TEXT] as? ToolState.TextState)
                ?.copy(selectedFontId = id) ?: ToolState.TextState(selectedFontId = id)

            currentState.copy(
                toolStates = currentState.toolStates + (EditorTabType.TEXT to textState)
            )
        }
    }
    val editorTabs = listOf(
        EditorTab(EditorTabType.TEXT, "Text", R.drawable.ic_text),
        EditorTab(EditorTabType.STYLE, "Style", R.drawable.ic_ai_gen),
        EditorTab(EditorTabType.BACKGROUND, "Background", R.drawable.ic_bg),
        EditorTab(EditorTabType.FLAG, "Flag", R.drawable.ic_ai_gen),
        EditorTab(EditorTabType.STICKER, "Sticker", R.drawable.ic_ai_gen),
        EditorTab(EditorTabType.NAME, "Name", R.drawable.ic_ai_gen),
        EditorTab(EditorTabType.TYPE, "Type", R.drawable.ic_ai_gen)
    )
    fun onTabSelected(type: EditorTabType) {
        _uiState.update { it.copy(selectedTab = type) }
    }
    fun onFilterSelected(id: String) {
        _selectedFilterId.value = id
        // Yahan aap logic laga sakte hain:
        // Agar "Car" select hua, to filtered list update karein
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        // Yahan aap trigger kar sakte hain -> searchRepository.search(newQuery)
    }
    private fun loadTemplates() {
        viewModelScope.launch {
            _templates.value = getTemplatesUseCase()
        }
    }
    private fun loadRecents() {
        viewModelScope.launch {
            try {
                _recents.value = getRecentPlatesUseCase()
            } catch (e: Exception) {
                // Yahan aap error handling add kar sakte hain
                e.printStackTrace()
            }
        }
    }
}
