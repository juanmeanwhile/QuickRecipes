package com.meanwhile.quickrecipes.domain

import com.meanwhile.quickrecipes.domain.model.Address
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

/**
 * Fake UseCase which returns the user Address. The real use cse would use a repository which
 * might trigger a request or get the data from Db. For this example, this works.
 */
class GetUserAddressUseCase @Inject constructor() {
    operator fun invoke(): Flow<Address> {
        println("GetUserAddressUseCase()")
        return flowOf(Address("Prime Street 19", "00019"))
    }
}