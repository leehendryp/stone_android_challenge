package com.leehendryp.stoneandroidchallenge.feed.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.leehendryp.stoneandroidchallenge.core.DTOs
import com.leehendryp.stoneandroidchallenge.core.RxUnitTest
import com.leehendryp.stoneandroidchallenge.feed.domain.usecases.SearchJokeUseCase
import com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel.FeedState
import com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel.FeedState.Default
import com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel.FeedState.Error
import com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel.FeedState.Loading
import com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel.FeedState.Success
import com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel.FeedViewModel
import io.mockk.every
import io.mockk.spyk
import io.mockk.verifyOrder
import io.reactivex.rxjava3.core.Flowable
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
        viewModel =
            FeedViewModel(
                compositeDisposable,
                searchJokeUseCase
            )
        initStateObserver()
    }

    private fun initStateObserver() {
        stateObserver = spyk()
        viewModel.state.subscribeWith(stateObserver)
    }

    @Test
    fun `should emit success state with joke list data upon successful use case execution`() {
        every { searchJokeUseCase.execute(any()) } returns DTOs.jokeFlowable

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

        every { searchJokeUseCase.execute(any()) } returns Flowable.error(error)

        viewModel.search("")

        verifyOrder {
            stateObserver.onNext(Default)
            stateObserver.onNext(Loading)
            stateObserver.onNext(Error(error))
            stateObserver.onNext(Default)
        }
    }
}
