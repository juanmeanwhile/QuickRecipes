package com.meanwhile.quickrecipes.ui

import com.meanwhile.quickrecipes.domain.model.Address
import com.meanwhile.quickrecipes.domain.model.Badge

data class UiState (
    val userBadges: List<Badge>? = null,
    val userAddress: Address? = null
)