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
import io.mockk.verify
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
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
    fun `when user is not logged then uiState is empty`() = runTest {
        every { getLoggedUserUseCase() } returns flowOf(null)

        val tested = createViewModel()
        val uiState = tested.uiState.value
        assertEquals(UiState(), uiState )
    }

    @Test
    fun `when user is not logged then getAddressUseCase is not executed`() = runTest {
        every { getLoggedUserUseCase() } returns flowOf(User("", ""))

        val tested = createViewModel()
        tested.uiState.take(2).first()
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
    fun `when user is logged then uiState contains address, badge and correct message`() = runTest {
        every { getLoggedUserUseCase() } returns flowOf(User("", ""))

        val tested = createViewModel()
        val uiState = tested.uiState.take(2).last()
        assertTrue(uiState.userAddress != null && uiState.userBadges != null)
    }

    private fun createViewModel() = AltMainViewModel(
        getLoggedUserUseCase, getUserAddressUseCase, getUserBadgesUseCase, doLoginUseCase, doLogoutUseCase
    )
}