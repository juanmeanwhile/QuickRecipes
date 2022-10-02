package com.meanwhile.quickrecipes.ui

import com.meanwhile.quickrecipes.domain.model.Address
import com.meanwhile.quickrecipes.domain.model.Badge

sealed class UiState {
    object Empty : UiState()

    data class LoggedIn (
        val userBadges: List<Badge>,
        val userAddress: Address
    ) : UiState()

    object LoggedOut : UiState()
}

