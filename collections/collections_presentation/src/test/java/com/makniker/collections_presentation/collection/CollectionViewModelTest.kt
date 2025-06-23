package com.makniker.collections_presentation.collection

import com.makniker.collections_domain.CollectionRepository
import com.makniker.collections_domain.StickerDTO
import com.makniker.core.ui.UiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CollectionViewModelTest {

    private lateinit var viewModel: CollectionViewModel
    private lateinit var repository: CollectionRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = CollectionViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadCollection should emit Loading state initially`() = runTest {
        // Given
        coEvery { repository.fetchSavedCollection() } returns Result.success(emptyList())

        // When
        viewModel.loadCollection()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is UiState.Loading)
    }

    @Test
    fun `loadCollection should emit Success state with mapped data when repository returns success`() = runTest {
        // Given
        val stickers = listOf(
            StickerDTO(id = 1, uri = "uri1", author = "author1", name = "name1"),
            StickerDTO(id = 2, uri = "uri2", author = "author2", name = "name2")
        )
        coEvery { repository.fetchSavedCollection() } returns Result.success(stickers)

        // When
        viewModel.loadCollection()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success)
        if (state is UiState.Success) {
            assertEquals(2, state.data.size)
            assertEquals(1, state.data[0].id)
            assertEquals("uri1", state.data[0].uri.toString())
            assertEquals(2, state.data[1].id)
            assertEquals("uri2", state.data[1].uri.toString())
        }
    }

    @Test
    fun `loadCollection should emit Failure state when repository returns error`() = runTest {
        // Given
        val exception = RuntimeException("Test error")
        coEvery { repository.fetchSavedCollection() } returns Result.failure(exception)

        // When
        viewModel.loadCollection()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is UiState.Failure)
        if (state is UiState.Failure) {
            assertEquals(exception, state.e)
        }
    }
} 