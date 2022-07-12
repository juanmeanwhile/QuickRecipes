package com.meanwhile.quickrecipes.data

import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun login()

    suspend fun logout()

    val loggedInUser: Flow<User?>

}