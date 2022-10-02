package com.meanwhile.quickrecipes

import com.meanwhile.quickrecipes.data.User
import com.meanwhile.quickrecipes.domain.DoLoginUseCase
import com.meanwhile.quickrecipes.domain.DoLogoutUseCase
import com.meanwhile.quickrecipes.domain.GetLoggedUserUseCase
import com.meanwhile.quickrecipes.domain.GetUserAddressUseCase
import com.meanwhile.quickrecipes.domain.GetUserBadgesUseCase
import com.meanwhile.quickrecipes.domain.model.Address
import com.meanwhile.quickrecipes.domain.model.Badge
import com.meanwhile.quickrecipes.ui.AltMainViewModel
import com.meanwhile.quickrecipes.ui.UiState
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.slot
import io.mockk.verify
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AltMainViewModelTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var getLoggedUserUseCase: GetLoggedUserUseCase

    @MockK
    lateinit var doLoginUseCase: DoLoginUseCase

    @MockK
    lateinit var doLogoutUseCase: DoLogoutUseCase

    @MockK
    lateinit var getUserAddressUseCase: GetUserAddressUseCase

    @MockK
    lateinit var getUserBadgesUseCase: GetUserBadgesUseCase

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)

        every { getUserAddressUseCase() } returns flowOf(Address("", ""))
        every { getUserBadgesUseCase() } returns flowOf(listOf(Badge("")))
    }

    @Test
    fun `when user is not logged then uiState is LoggedOut`() = runTest {
        every { getLoggedUserUseCase() } returns flowOf(null)

        val tested = createViewModel()
        val emittedStates = tested.uiState.take(2).toList()

        assertEquals(UiState.Empty, emittedStates[0])
        assertEquals(UiState.LoggedOut, emittedStates[1])
    }

    @Test
    fun `when user is not logged then getAddressUseCase is not executed`() = runTest {
        every { getLoggedUserUseCase() } returns flowOf(null)

        val tested = createViewModel()
        tested.uiState.take(2) // Empty and UiLoggedOut
        verify(exactly = 0) { getUserAddressUseCase() }
    }

    @Test
    fun `when user is logged then getAddressUseCase is executed`() = runTest {
        every { getLoggedUserUseCase() } returns flowOf(User("", ""))

        val tested = createViewModel()
        tested.uiState.take(2).last()
        verify(exactly = 1) { getUserAddressUseCase() }
    }

    @Test
    fun `when user goes from logged in to not logged in then use cases are cancelled`() = runTest {
        every { getLoggedUserUseCase() } returns flow {
            emit(User("", ""))
            delay(50)
            emit(null)
        }

        // add some delay to usecases so we can cancel them
        every { getUserAddressUseCase() } returns flow {
            print("started")
            delay(100)
            emit(Address("street", "0"))
        }.onCompletion { print("$it") }

        every { getUserBadgesUseCase() } returns flow {
            delay(100)
            emit(listOf())
        }

        val tested = createViewModel()
        val emittedStates = tested.uiState.take(2).toList()

        // If last UI state is LoggedOute, then the useCses where not finished
        assertEquals(UiState.Empty, emittedStates[0])
        assertEquals(UiState.LoggedOut, emittedStates[1])
    }

    @Test
    fun `when user is logged then uiState is LoggedIn`() = runTest {
        every { getLoggedUserUseCase() } returns flowOf(User("", ""))

        val tested = createViewModel()
        val uiStates = tested.uiState.take(2).toList()
        assertEquals(UiState.Empty, uiStates[0])
        assertTrue(uiStates[1] is UiState.LoggedIn)
    }

    private fun createViewModel() = AltMainViewModel(
        getLoggedUserUseCase, getUserAddressUseCase, getUserBadgesUseCase, doLoginUseCase, doLogoutUseCase
    )
}