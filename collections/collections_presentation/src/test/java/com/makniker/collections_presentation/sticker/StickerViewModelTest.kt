package com.makniker.collections_presentation.sticker

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
class StickerViewModelTest {

    private lateinit var viewModel: StickerViewModel
    private lateinit var repository: CollectionRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = StickerViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadSticker should emit Loading state initially`() = runTest {
        // Given
        val stickerId = 1
        coEvery { repository.getSticker(stickerId) } returns Result.success(
            StickerDTO(id = stickerId, uri = "uri1", author = "author1", name = "name1")
        )

        // When
        viewModel.loadSticker(stickerId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is UiState.Loading)
    }

    @Test
    fun `loadSticker should emit Success state with mapped data when repository returns success`() = runTest {
        // Given
        val stickerId = 1
        val sticker = StickerDTO(id = stickerId, uri = "uri1", author = "author1", name = "name1")
        coEvery { repository.getSticker(stickerId) } returns Result.success(sticker)

        // When
        viewModel.loadSticker(stickerId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success)
        if (state is UiState.Success) {
            assertEquals(stickerId, state.data.id)
            assertEquals("uri1", state.data.uri.toString())
            assertEquals("name1", state.data.name)
            assertEquals("author1", state.data.author)
        }
    }

    @Test
    fun `loadSticker should emit Failure state when repository returns error`() = runTest {
        // Given
        val stickerId = 1
        val exception = RuntimeException("Test error")
        coEvery { repository.getSticker(stickerId) } returns Result.failure(exception)

        // When
        viewModel.loadSticker(stickerId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is UiState.Failure)
        if (state is UiState.Failure) {
            assertEquals(exception, state.e)
        }
    }
} 