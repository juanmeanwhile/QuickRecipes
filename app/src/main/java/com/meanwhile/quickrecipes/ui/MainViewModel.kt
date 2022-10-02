package com.meanwhile.quickrecipes.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meanwhile.quickrecipes.domain.DoLoginUseCase
import com.meanwhile.quickrecipes.domain.DoLogoutUseCase
import com.meanwhile.quickrecipes.domain.GetLoggedUserUseCase
import com.meanwhile.quickrecipes.domain.GetUserAddressUseCase
import com.meanwhile.quickrecipes.domain.GetUserBadgesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(
    getLoggedUserUseCase: GetLoggedUserUseCase,
    private val getUserAddressUseCase: GetUserAddressUseCase,
    private val getUserBadgesUseCase: GetUserBadgesUseCase,
    private val doLoginUseCase: DoLoginUseCase,
    private val doLogoutUseCase: DoLogoutUseCase
    ): ViewModel() {

    val uiState = getLoggedUserUseCase()
        .map { user -> user != null }
        .distinctUntilChanged()
        .flatMapLatest { isLoggedIn ->
            if (isLoggedIn) {
                // We trigger our use cases. Use combine to merge all the outputs in our UiState
                combine(getUserBadgesUseCase(), getUserAddressUseCase()){ badges, address ->
                    UiState.LoggedIn( userBadges = badges, userAddress = address)
                }
            } else {
                // Return the UiState which represents the user is logged out
                flowOf(UiState.LoggedOut)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UiState.Empty)

    fun onLoginClicked() {
        viewModelScope.launch {
            doLoginUseCase()
        }
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            doLogoutUseCase()
        }
    }
}