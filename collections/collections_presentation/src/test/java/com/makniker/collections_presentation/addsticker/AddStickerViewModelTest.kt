package com.makniker.collections_presentation.addsticker

import com.makniker.collections_domain.CollectionRepository
import com.makniker.collections_domain.StickerDTO
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddStickerViewModelTest {

    private lateinit var viewModel: AddStickerViewModel
    private lateinit var repository: CollectionRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = AddStickerViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `updateName should update name state`() {
        // Given
        val newName = "Test Sticker"

        // When
        viewModel.updateName(newName)

        // Then
        assertEquals(newName, viewModel.name)
    }

    @Test
    fun `updateAuthor should update author state`() {
        // Given
        val newAuthor = "Test Author"

        // When
        viewModel.updateAuthor(newAuthor)

        // Then
        assertEquals(newAuthor, viewModel.author)
    }

    @Test
    fun `updateUri should update uri state`() {
        // Given
        val newUri = "content://test/uri"

        // When
        viewModel.updateUri(newUri)

        // Then
        assertEquals(newUri, viewModel.uri)
    }

    @Test
    fun `isFormValid should return false when name is empty`() {
        // Given
        viewModel.updateName("")
        viewModel.updateAuthor("Test Author")

        // When
        val isValid = viewModel.isFormValid()

        // Then
        assertFalse(isValid)
    }

    @Test
    fun `isFormValid should return false when author is empty`() {
        // Given
        viewModel.updateName("Test Sticker")
        viewModel.updateAuthor("")

        // When
        val isValid = viewModel.isFormValid()

        // Then
        assertFalse(isValid)
    }

    @Test
    fun `isFormValid should return true when both name and author are not empty`() {
        // Given
        viewModel.updateName("Test Sticker")
        viewModel.updateAuthor("Test Author")

        // When
        val isValid = viewModel.isFormValid()

        // Then
        assertTrue(isValid)
    }

    @Test
    fun `saveSticker should call repository with correct data`() = runTest {
        // Given
        val name = "Test Sticker"
        val author = "Test Author"
        val uri = "content://test/uri"
        var onEndCalled = false

        viewModel.updateName(name)
        viewModel.updateAuthor(author)
        viewModel.updateUri(uri)

        coEvery { repository.addSticker(any()) } returns Result.success(Unit)

        // When
        viewModel.saveSticker { onEndCalled = true }
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { 
            repository.addSticker(
                match { sticker ->
                    sticker.name == name &&
                    sticker.author == author &&
                    sticker.uri == uri &&
                    sticker.id == null
                }
            )
        }
        assertTrue(onEndCalled)
    }
} 