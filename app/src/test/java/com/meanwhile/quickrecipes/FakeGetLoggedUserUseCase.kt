package com.meanwhile.quickrecipes

import com.meanwhile.quickrecipes.data.User
import com.meanwhile.quickrecipes.domain.GetLoginStateUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeGetLoggedUserUseCase : GetLoginStateUseCase {

    private val mutableSharedFlow = MutableSharedFlow<User?>()

    suspend fun emit(user: User?){
        mutableSharedFlow.emit(user)
    }

    override operator fun invoke(): Flow<User?> = mutableSharedFlow
}