package com.leehendryp.stoneandroidchallenge.feed.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.leehendryp.stoneandroidchallenge.core.BadRequestException
import com.leehendryp.stoneandroidchallenge.core.DTOs
import com.leehendryp.stoneandroidchallenge.core.RxUnitTest
import com.leehendryp.stoneandroidchallenge.feed.data.toJokeList
import com.leehendryp.stoneandroidchallenge.feed.domain.JokeRepository
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Maybe
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
        useCase =
            SearchJokeUseCaseImpl(
                repository
            )
    }

    @Test
    fun `should return joke list from successful repository data fetch attempt`() {
        val jokeList = DTOs.correctJokeResponses.toJokeList()

        every { repository.search(any()) } returns Maybe.just(jokeList)

        useCase.execute("")
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertResult(jokeList)
    }

    @Test
    fun `should return error from failed repository data fetch attempt`() {
        val error = BadRequestException("")

        every { repository.search(any()) } returns Maybe.error(error)

        useCase.execute("")
            .test()
            .assertNotComplete()
            .assertError { it == error }
    }
}