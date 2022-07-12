package com.meanwhile.quickrecipes.domain

import javax.inject.Inject
import kotlinx.coroutines.delay

class FetchSomethingUseCase @Inject constructor(){

    suspend operator fun invoke(): Unit {
        println("FetchSomethingUseCase has started")
        delay(5000)
        println("FetchSomethingUseCase has ended")
    }
}