package com.meanwhile.quickrecipes.domain

import com.meanwhile.quickrecipes.data.UserRepository
import javax.inject.Inject

class DoLogoutUseCase @Inject constructor(val userRepository: UserRepository) {

    suspend operator fun invoke() {
        userRepository.logout()
    }
}