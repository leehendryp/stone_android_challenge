package com.leehendryp.stoneandroidchallenge.feed.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.leehendryp.stoneandroidchallenge.core.DTOs
import com.leehendryp.stoneandroidchallenge.core.RxUnitTest
import com.leehendryp.stoneandroidchallenge.feed.domain.JokeRepository
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchJokeUseCaseImplTest : RxUnitTest() {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: JokeRepository
    private lateinit var useCase: SearchJokeUseCase

    @Before
    fun `set up`() {
        repository = mockk()
        useCase = SearchJokeUseCaseImpl(repository)
    }

    @Test
    fun `should return joke list from successful repository data fetch attempt`() {
        every { repository.search(any()) } returns DTOs.jokeFlowable

        useCase.execute("")
            .test()
            .assertComplete()
            .assertNoErrors()
            .values()
            .containsAll(DTOs.jokes)
    }

    @Test
    fun `should return error from failed repository data fetch attempt`() {
        val error = Throwable()

        every { repository.search(any()) } returns Flowable.error(error)

        useCase.execute("")
            .test()
            .assertNotComplete()
            .assertError(error)
    }
}
