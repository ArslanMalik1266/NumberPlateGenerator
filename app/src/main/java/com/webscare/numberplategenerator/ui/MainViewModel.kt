package com.webscare.numberplategenerator.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.webscare.numberplategenerator.R
import com.webscare.numberplategenerator.data.datasource.AssetDataProvider
import com.webscare.numberplategenerator.domain.model.DimensionOption
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
        EditorTab(EditorTabType.DIMENSION, "Dimensions", R.drawable.ic_ai_gen),
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





    fun onSearchQueryChange(newQuery: String) {
        _uiState.update { it.copy(searchQuery = newQuery) }
    }

    fun updatePlateDimension(option: DimensionOption) {
        _editorState.update { currentState ->
            val currentDimState = (currentState.toolStates[EditorTabType.DIMENSION] as? ToolState.DimensionState)
                ?: ToolState.DimensionState()

            // Update specific slot based on current context
            val updatedDimState = when (currentState.plateType) {
                PlateType.CAR -> {
                    if (currentState.isFront) {
                        currentDimState.copy(carFrontId = option.id)
                    } else {
                        currentDimState.copy(carBackId = option.id)
                    }
                }
                PlateType.BIKE -> {
                    if (currentState.isFront) {
                        currentDimState.copy(bikeFrontId = option.id)
                    } else {
                        currentDimState.copy(bikeBackId = option.id)
                    }
                }
            }

            currentState.copy(
                toolStates = currentState.toolStates + (EditorTabType.DIMENSION to updatedDimState)
            )
        }
    }

    fun onHeaderLongPress(isPressed: Boolean) {
        _editorState.update { it.copy(isLongPressingHeader = isPressed) }
    }

    fun updateSide(isFront: Boolean) {
        _editorState.update { currentState ->
            val dimState = (currentState.toolStates[EditorTabType.DIMENSION] as? ToolState.DimensionState)
                ?: ToolState.DimensionState()

            currentState.copy(
                isFront = isFront,
                toolStates = currentState.toolStates + (EditorTabType.DIMENSION to dimState)
            )
        }
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
    fun onBackgroundSelect(bgId: String) {
        _editorState.update { currentState ->
            val currentBgState = (currentState.toolStates[EditorTabType.BACKGROUND] as? ToolState.BackgroundState)
                ?: ToolState.BackgroundState()

            val updatedBgState = currentBgState.copy(selectedBackgroundId = bgId)

            currentState.copy(
                toolStates = currentState.toolStates + (EditorTabType.BACKGROUND to updatedBgState)
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
    fun onFlagSelect(id: String, resId: Int?) {
        val currentState = _editorState.value
        val newFlagState = (currentState.toolStates[EditorTabType.FLAG] as? ToolState.FlagState)
            ?.copy(
                selectedFlagId = id,
                selectedFlagRes = resId // Preview ko resId yahan se milega
            ) ?: ToolState.FlagState(selectedFlagId = id, selectedFlagRes = resId)

        _editorState.value = currentState.copy(
            toolStates = currentState.toolStates + (EditorTabType.FLAG to newFlagState)
        )
    }

    fun onStickerSelect(id: String, resId: Int?) {
        _editorState.update { currentState ->
            val currentStickerState = (currentState.toolStates[EditorTabType.STICKER] as? ToolState.StickerState)
                ?: ToolState.StickerState()

            val updatedStickerState = currentStickerState.copy(
                selectedStickerId = id,
                selectedStickerRes = resId,
                stickerTint = currentStickerState.stickerTint
            )

            currentState.copy(
                toolStates = currentState.toolStates + (EditorTabType.STICKER to updatedStickerState)
            )
        }
    }
    fun onStickerColorSelect(colorInt: Int) {
        _editorState.update { currentState ->
            val currentStickerState = (currentState.toolStates[EditorTabType.STICKER] as? ToolState.StickerState)
                ?: ToolState.StickerState()

            val updatedStickerState = currentStickerState.copy(
                stickerTint = colorInt // Naya color apply hoga
            )

            currentState.copy(
                toolStates = currentState.toolStates + (EditorTabType.STICKER to updatedStickerState)
            )
        }
    }

    fun resetEditorState() {
        _editorState.update { currentState ->
            // 1. Sirf fonts ko preserve karein kyunki wo network/source se aate hain
            val currentFonts = (currentState.toolStates[EditorTabType.TEXT] as? ToolState.TextState)?.fontOptions ?: emptyList()

            // 2. Default state banayein
            val defaultState = EditorStates()

            // 3. Senior Approach: Hardcoding ki jagah Data Source se pehla valid item nikaalein
            // Hum filter kar rahe hain taake Car ke liye Car ki aur Bike ke liye Bike ki default dimension aaye



            val resetTextState = ToolState.TextState(
                fontOptions = currentFonts,
                selectedFontId = currentFonts.firstOrNull()?.id ?: "1"
            )

            // 4. State update: merge current preserved data with fresh defaults
            defaultState.copy(
                plateType = currentState.plateType, // Current type (Car/Bike) barkrar rakhein
                toolStates = defaultState.toolStates + mapOf(
                    EditorTabType.TEXT to resetTextState,
                )
            )
        }
    }

    fun onOwnerNameChange(newName: String) {
        _editorState.update { currentState ->
            val currentNameState = (currentState.toolStates[EditorTabType.NAME] as? ToolState.NameState)
                ?: ToolState.NameState()

            val updatedNameState = currentNameState.copy(ownerName = newName)


            currentState.copy(
                toolStates = currentState.toolStates + (EditorTabType.NAME to updatedNameState)
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
                        selectedFontId = ""
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