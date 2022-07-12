package com.meanwhile.quickrecipes.data

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * A repository simulating login and exposing a user flow. Stores a flow internally for simplicity, but the source of truth should be
 * the account manager
 */
class UserRepositoryImp @Inject constructor(): UserRepository {

    override suspend fun login() {
        val fakeUser = User("a123", "Finn the Human")

        _loggedInUser.emit(fakeUser)
    }

    override suspend fun logout() {
        _loggedInUser.emit(null)
    }

    private val _loggedInUser = MutableStateFlow<User?>(null)

    /**
     * Exposes the currently logged [User] if any or null if there isn't any
     */
    override val loggedInUser: Flow<User?> = _loggedInUser
}