package com.meanwhile.quickrecipes.domain

import javax.inject.Inject
import kotlinx.coroutines.delay

class DoOtherThingUseCase @Inject constructor(){

    suspend operator fun invoke(): Unit {
        println("DoOtherThingUseCase has started")
        delay(4000)
        println("DoOtherThingUseCase has ended")
    }
}