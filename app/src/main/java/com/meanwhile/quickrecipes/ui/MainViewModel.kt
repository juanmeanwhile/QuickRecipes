package com.meanwhile.quickrecipes.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meanwhile.quickrecipes.domain.DoLoginUseCase
import com.meanwhile.quickrecipes.domain.DoLogoutUseCase
import com.meanwhile.quickrecipes.domain.DoOtherThingUseCase
import com.meanwhile.quickrecipes.domain.FetchSomethingUseCase
import com.meanwhile.quickrecipes.domain.GetLoggedUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getLoggedUserUseCase: GetLoggedUserUseCase ,
    private val fetchSomethingUseCase: FetchSomethingUseCase,
    private val doOtherThingUseCase: DoOtherThingUseCase,
    private val doLoginUseCase: DoLoginUseCase,
    private val doLogoutUseCase: DoLogoutUseCase
): ViewModel() {

    init {
        viewModelScope.launch {
            getLoggedUserUseCase() // returns Flow<User?>
                .map { user -> user != null } // when the user is not null, we'll emit true
                .distinctUntilChanged() // we only want to emit changes in the login state, from logged in to logged out and the other way around
                .collectLatest { isLoggedIn -> // Cancel previous execution if is still in progress
                    if (isLoggedIn) {
                        // We trigger our use cases.
                        fetchSomethingUseCase()
                        doOtherThingUseCase()
                    }
                }
        }
    }

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