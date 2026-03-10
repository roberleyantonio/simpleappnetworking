package br.com.dev360.simpleapplicationnetworking.feature.characterlist.presentation

import app.cash.turbine.test
import br.com.dev360.simpleapplicationnetworking.MainDispatcherRule
import br.com.dev360.simpleapplicationnetworking.core.shared.exception.Failure
import br.com.dev360.simpleapplicationnetworking.feature.MORTY
import br.com.dev360.simpleapplicationnetworking.feature.RICK
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.domain.CharacterListUseCase
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.domain.model.CharacterList
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.domain.model.Info
import br.com.dev360.simpleapplicationnetworking.feature.fakeCharacter
import br.com.dev360.simpleapplicationnetworking.feature.fakeCharacterList
import br.com.dev360.simpleapplicationnetworking.feature.fakeInfo
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val useCase: CharacterListUseCase = mockk()
    private lateinit var viewModel: CharacterListViewModel

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `init block should load characters and update uiState on success`() = runTest {
        coEvery { useCase.getCharacters(1) } returns Result.success(fakeCharacterList)

        viewModel = CharacterListViewModel(useCase)

        viewModel.uiState.test {
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)
            assertTrue(loadingState.characters.isEmpty())

            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertEquals(1, successState.characters.size)
            assertEquals(RICK, successState.characters.first().name)
        }
    }

    @Test
    fun `loadCharacters should append items and increment page when paginating`() = runTest {
        val page2Character = fakeCharacter.copy(id = 2, name = MORTY)
        val page2List = CharacterList(info = fakeInfo, results = listOf(page2Character))

        coEvery { useCase.getCharacters(1) } returns Result.success(fakeCharacterList)
        coEvery { useCase.getCharacters(2) } returns Result.success(page2List)

        viewModel = CharacterListViewModel(useCase)

        viewModel.loadCharacters()

        viewModel.uiState.test {
            skipItems(2)

            viewModel.loadCharacters()

            val paginatingState = awaitItem()
            assertTrue(paginatingState.isPaginatedLoading)
            assertEquals(1, paginatingState.characters.size)

            val successState = awaitItem()
            assertFalse(successState.isPaginatedLoading)
            assertEquals(2, successState.characters.size)
            assertEquals(MORTY, successState.characters[1].name)
        }
    }

    @Test
    fun `loadCharacters should update state with Failure when useCase fails`() = runTest {
        val exception = IOException("No internet")
        coEvery { useCase.getCharacters(1) } returns Result.failure(exception)

        viewModel = CharacterListViewModel(useCase)

        viewModel.uiState.test {
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertTrue(errorState.error is Failure.NetworkConnection)
        }
    }

    @Test
    fun `loadCharacters should not call useCase if cannot paginate anymore`() = runTest {
        val singlePageInfo = Info(count = 20, pages = 1)
        val singlePageList = CharacterList(info = singlePageInfo, results = listOf(fakeCharacter))

        coEvery { useCase.getCharacters(any()) } returns Result.success(singlePageList)

        viewModel = CharacterListViewModel(useCase)

        viewModel.uiState.test {
            assertTrue(awaitItem().isLoading)
            assertFalse(awaitItem().isLoading)

            viewModel.loadCharacters()

            expectNoEvents()

            coVerify(exactly = 1) { useCase.getCharacters(any()) }
        }
    }
}