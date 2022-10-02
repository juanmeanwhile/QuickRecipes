package com.meanwhile.quickrecipes.domain

import com.meanwhile.quickrecipes.data.User
import kotlinx.coroutines.flow.Flow

interface GetLoginStateUseCase {

    operator fun invoke() : Flow<User?>
}