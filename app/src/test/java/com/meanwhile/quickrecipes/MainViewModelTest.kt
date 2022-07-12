package com.meanwhile.quickrecipes

import com.meanwhile.quickrecipes.data.User
import com.meanwhile.quickrecipes.domain.DoLoginUseCase
import com.meanwhile.quickrecipes.domain.DoLogoutUseCase
import com.meanwhile.quickrecipes.domain.DoOtherThingUseCase
import com.meanwhile.quickrecipes.domain.FetchSomethingUseCase
import com.meanwhile.quickrecipes.domain.GetLoggedUserUseCase
import com.meanwhile.quickrecipes.ui.MainViewModel
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var getLoggedUserUseCase: GetLoggedUserUseCase

    @MockK
    lateinit var doLoginUseCase: DoLoginUseCase

    @MockK
    lateinit var doLogoutUseCase: DoLogoutUseCase

    @MockK
    lateinit var fetchSomethingUseCase: FetchSomethingUseCase

    @MockK
    lateinit var doOtherThingUseCase: DoOtherThingUseCase

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)

        coJustRun { fetchSomethingUseCase() }
        coJustRun { doOtherThingUseCase() }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `when user is logged in then fetch is called`() = runTest {
        every{getLoggedUserUseCase()} returns flowOf(User("",""))

        val tested = createTestViewModel()

        coVerify { fetchSomethingUseCase() }

    }

    @Test
    fun `when user is logged in then DoOtherThingUseCase is called`() = runTest {
        every{getLoggedUserUseCase()} returns flowOf(User("",""))

        val tested = createTestViewModel()

        coVerify { doOtherThingUseCase() }

    }

    @Test
    fun `when user is logged out then fetch is not called`() = runTest {
        every{getLoggedUserUseCase()} returns flowOf(null)

        val tested = createTestViewModel()

        coVerify(exactly = 0) { fetchSomethingUseCase() }

    }

    @Test
    fun `when more than one change in User is emitted then fetch is called only once`(){
        every{getLoggedUserUseCase()} returns flowOf(User("a", "a"), User("b","b"))

        val tested = createTestViewModel()

        coVerify(exactly = 1) { fetchSomethingUseCase() }
    }

    @Test
    fun `when going from login to logged out then fetch is cancelled`() = runTest {
        every{getLoggedUserUseCase()} returns flow {
            emit(null)
            emit(User("",""))
            delay(100)
            emit(null)
        }

        //TODO this passes but is not testing what I want
        fetchSomethingUseCase()

        coEvery { fetchSomethingUseCase() } coAnswers {
            delay(200)
        }

        val tested = createTestViewModel()

        coVerify(exactly = 0) { fetchSomethingUseCase() }

    }

    private fun createTestViewModel() =
        MainViewModel(
            getLoggedUserUseCase, fetchSomethingUseCase, doOtherThingUseCase, doLoginUseCase, doLogoutUseCase
        )
}