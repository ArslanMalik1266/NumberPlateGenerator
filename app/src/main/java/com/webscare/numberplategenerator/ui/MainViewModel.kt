package com.webscare.numberplategenerator.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.webscare.numberplategenerator.domain.model.PlateTemplate
import com.webscare.numberplategenerator.domain.model.RecentPlate
import com.webscare.numberplategenerator.domain.usecase.GetRecentPlatesUseCase
import com.webscare.numberplategenerator.domain.usecase.GetTemplatesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    init {
        loadTemplates()
        loadRecents()
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
