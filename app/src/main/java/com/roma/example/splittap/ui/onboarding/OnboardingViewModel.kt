package com.roma.example.splittap.ui.onboarding

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private const val OnboardingPageCount = 4

data class OnboardingUiState(
    val currentPage: Int = 0,
    val pageCount: Int = OnboardingPageCount
) {
    val isLastPage: Boolean
        get() = currentPage == pageCount - 1
}

class OnboardingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun showNextPage(): Boolean {
        val state = _uiState.value
        if (state.isLastPage) return true

        _uiState.update { it.copy(currentPage = it.currentPage + 1) }
        return false
    }

    fun reset() {
        _uiState.update { it.copy(currentPage = 0) }
    }
}
