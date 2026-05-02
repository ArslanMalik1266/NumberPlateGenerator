package com.webscare.numberplategenerator.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.webscare.numberplategenerator.R
import com.webscare.numberplategenerator.domain.model.EditorTabType
import com.webscare.numberplategenerator.domain.model.FontOption
import com.webscare.numberplategenerator.domain.model.PlateTemplate
import com.webscare.numberplategenerator.domain.model.RecentPlate
import com.webscare.numberplategenerator.domain.usecase.GetRecentPlatesUseCase
import com.webscare.numberplategenerator.domain.usecase.GetTemplatesUseCase
import com.webscare.numberplategenerator.domain.usecase.GetUrduFontsUseCase
import com.webscare.numberplategenerator.ui.editor.EditorTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MainViewModel(
    private val getTemplatesUseCase: GetTemplatesUseCase,
    private val getRecentPlatesUseCase: GetRecentPlatesUseCase,
    private val getUrduFontsUseCase: GetUrduFontsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainState())
    val uiState = _uiState.asStateFlow()

    private val _editorState = MutableStateFlow(EditorStates())
    val editorState = _editorState.asStateFlow()

    val editorTabs = listOf(
        EditorTab(EditorTabType.TEXT, "Text", R.drawable.ic_text),
        EditorTab(EditorTabType.STYLE, "Style", R.drawable.ic_ai_gen),
        EditorTab(EditorTabType.BACKGROUND, "Background", R.drawable.ic_bg),
        EditorTab(EditorTabType.FLAG, "Flag", R.drawable.ic_ai_gen),
        EditorTab(EditorTabType.STICKER, "Sticker", R.drawable.ic_ai_gen),
        EditorTab(EditorTabType.NAME, "Name", R.drawable.ic_ai_gen),
        EditorTab(EditorTabType.TYPE, "Type", R.drawable.ic_ai_gen)
    )

    init {
        loadTemplates()
        loadRecents()
        loadFonts()
    }

    // --- Main State Updates ---

    fun onSearchQueryChange(newQuery: String) {
        _uiState.update { it.copy(searchQuery = newQuery) }
    }

    fun onFilterSelected(id: String) {
        _uiState.update { it.copy(selectedFilterId = id) }
    }

    fun toggleFab() {
        _uiState.update { it.copy(isFabExpanded = !it.isFabExpanded) }
    }

    // --- Editor State Updates ---

    fun onColorSelect(colorInt: Int) {
        _editorState.update { currentState ->
            val currentStyle = (currentState.toolStates[EditorTabType.STYLE] as? ToolState.StyleState)
                ?: ToolState.StyleState()

            val updatedStyle = currentStyle.copy(selectedColor = colorInt)

            currentState.copy(
                toolStates = currentState.toolStates + (EditorTabType.STYLE to updatedStyle)
            )
        }
    }

    fun updatePlateType(type: PlateType) {
        _editorState.value = _editorState.value.copy(plateType = type)
    }

    fun onTextChange(newText: String) {
        _editorState.update { it.copy(plateText = newText) }
    }

    fun onFontSelect(id: String) {
        _editorState.update { currentState ->
            val textState = (currentState.toolStates[EditorTabType.TEXT] as? ToolState.TextState)
            val newSelectedFont = textState?.fontOptions?.find { it.id == id }
            val updatedTextState = textState?.copy(
                selectedFontId = id,
                selectedFont = newSelectedFont // Poora object store kar liya
            ) ?: ToolState.TextState(selectedFontId = id, selectedFont = newSelectedFont)

            currentState.copy(
                toolStates = currentState.toolStates + (EditorTabType.TEXT to updatedTextState)
            )
        }
    }

    fun onTabSelected(type: EditorTabType) {
        _editorState.update { it.copy(selectedTab = type) }
    }

    // --- Data Loading ---

    private fun loadTemplates() {
        viewModelScope.launch {
            val templates = getTemplatesUseCase()
            _uiState.update { it.copy(templates = templates) }
        }
    }

    private fun loadRecents() {
        viewModelScope.launch {
            try {
                val recents = getRecentPlatesUseCase()
                _uiState.update { it.copy(recents = recents) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleBold() {
        _editorState.update { currentState ->
            val currentStyle = (currentState.toolStates[EditorTabType.STYLE] as? ToolState.StyleState)
                ?: ToolState.StyleState()

            val updatedStyle = currentStyle.copy(isBold = !currentStyle.isBold)

            currentState.copy(
                toolStates = currentState.toolStates + (EditorTabType.STYLE to updatedStyle)
            )
        }
    }

    fun toggleItalic() {
        _editorState.update { currentState ->
            val currentStyle = (currentState.toolStates[EditorTabType.STYLE] as? ToolState.StyleState)
                ?: ToolState.StyleState()

            val updatedStyle = currentStyle.copy(isItalic = !currentStyle.isItalic)

            currentState.copy(
                toolStates = currentState.toolStates + (EditorTabType.STYLE to updatedStyle)
            )
        }
    }

    fun toggleUnderline() {
        _editorState.update { currentState ->
            val currentStyle = (currentState.toolStates[EditorTabType.STYLE] as? ToolState.StyleState)
                ?: ToolState.StyleState()

            val updatedStyle = currentStyle.copy(isUnderline = !currentStyle.isUnderline)

            currentState.copy(
                toolStates = currentState.toolStates + (EditorTabType.STYLE to updatedStyle)
            )
        }
    }

    private fun loadFonts() {
        viewModelScope.launch {
            try {
                println("DEBUG_NET: loadFonts() started in ViewModel")
                val fonts = getUrduFontsUseCase()
                println("DEBUG_NET: Data received! Size: ${fonts.size}")
                fonts.forEach { font ->
                    println("DEBUG_NET: Font ID: ${font.id}, Name: ${font.name}")
                }

                _editorState.update { currentState ->
                    val currentTextState = (currentState.toolStates[EditorTabType.TEXT] as? ToolState.TextState)
                        ?: ToolState.TextState()

                    val updatedTextState = currentTextState.copy(
                        fontOptions = fonts,
                        selectedFontId = fonts.firstOrNull()?.id ?: "1"
                    )
                    println("DEBUG_NET: State update dispatched to UI")
                    currentState.copy(
                        toolStates = currentState.toolStates + (EditorTabType.TEXT to updatedTextState)
                    )
                }
            } catch (e: Exception) {
                println("DEBUG_NET: Error in loadFonts: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}