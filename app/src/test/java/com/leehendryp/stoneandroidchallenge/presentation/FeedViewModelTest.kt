package com.leehendryp.stoneandroidchallenge.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.leehendryp.stoneandroidchallenge.core.DTOs
import com.leehendryp.stoneandroidchallenge.core.RxUnitTest
import com.leehendryp.stoneandroidchallenge.feed.domain.usecases.SearchJokeUseCase
import com.leehendryp.stoneandroidchallenge.presentation.FeedState.Default
import com.leehendryp.stoneandroidchallenge.presentation.FeedState.Error
import com.leehendryp.stoneandroidchallenge.presentation.FeedState.Loading
import com.leehendryp.stoneandroidchallenge.presentation.FeedState.Success
import io.mockk.every
import io.mockk.spyk
import io.mockk.verifyOrder
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FeedViewModelTest : RxUnitTest() {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var searchJokeUseCase: SearchJokeUseCase
    private lateinit var viewModel: FeedViewModel

    private lateinit var stateObserver: Observer<FeedState>

    @Before
    fun `set up`() {
        searchJokeUseCase = spyk()
        compositeDisposable = spyk()
        viewModel = FeedViewModel(compositeDisposable, searchJokeUseCase)
        initStateObserver()
    }

    private fun initStateObserver() {
        stateObserver = spyk()
        viewModel.state.subscribeWith(stateObserver)
    }

    @Test
    fun `should emit success state with joke list data upon successful use case execution`() {
        every { searchJokeUseCase.execute(any()) } returns Maybe.just(DTOs.jokes)

        viewModel.search("")

        verifyOrder {
            stateObserver.onNext(Default)
            stateObserver.onNext(Loading)
            stateObserver.onNext(Success(DTOs.jokes))
            stateObserver.onNext(Default)
        }
    }

    @Test
    fun `should emit error state with error data upon failed use case execution`() {
        val error = Throwable()

        every { searchJokeUseCase.execute(any()) } returns Maybe.error(error)

        viewModel.search("")

        verifyOrder {
            stateObserver.onNext(Default)
            stateObserver.onNext(Loading)
            stateObserver.onNext(Error(error))
            stateObserver.onNext(Default)
        }
    }
}
