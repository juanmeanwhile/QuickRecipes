package com.meanwhile.quickrecipes.domain

import com.meanwhile.quickrecipes.domain.model.Badge
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Fake UseCase which returns a list of badges. The real use cse would use a repository which
 * might trigger a request or get the data from Db. For this example, this works.
 */
class GetUserBadgesUseCase @Inject constructor() {
    operator fun invoke(): Flow<List<Badge>> {
        println("GetUserBadgesUseCase()")
        return flowOf(listOf(Badge("gold"), Badge("silver")))
    }
}