package com.meanwhile.quickrecipes.domain

import com.meanwhile.quickrecipes.data.User
import com.meanwhile.quickrecipes.data.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Return user information or null if the user is not logged in
 */
class GetLoggedUserUseCase @Inject constructor(private val userRepository: UserRepository): GetLoginStateUseCase {

    override operator fun invoke() : Flow<User?> = userRepository.loggedInUser
}