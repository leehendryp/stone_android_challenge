package com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.leehendryp.stoneandroidchallenge.core.extensions.observeOnMain
import com.leehendryp.stoneandroidchallenge.core.extensions.subscribeOnIO
import com.leehendryp.stoneandroidchallenge.feed.domain.usecases.SearchJokeUseCase
import com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel.FeedState.Default
import com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel.FeedState.Error
import com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel.FeedState.Loading
import com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel.FeedState.Success
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class FeedViewModel @Inject constructor(
    private val compositeDisposable: CompositeDisposable,
    private val searchJokeUseCase: SearchJokeUseCase
) : ViewModel() {
    companion object {
        private const val BUFFER_COUNT = 20
    }

    val state: BehaviorSubject<FeedState> by lazy { initState() }
    private var shouldWarnNoResult: Boolean = false

    fun search(query: String) {
        compositeDisposable.add(
            searchJokeUseCase.execute(query)
                .subscribeOnIO()
                .observeOnMain()
                .doOnSubscribe {
                    state(Loading)
                    warnNoResult(true)
                }
                .doFinally { state(Default(shouldWarnNoResult)) }
                .buffer(BUFFER_COUNT)
                .subscribeBy(
                    onNext = { jokeList -> state(Success(jokeList)) },
                    onError = { error ->
                        state(Error(error))
                        warnNoResult(false)
                    }
                )
        )
    }

    private fun initState() = BehaviorSubject.create<FeedState>()

    private fun state(feedState: FeedState) = state.onNext(feedState)

    private fun warnNoResult(warn: Boolean) {
        shouldWarnNoResult = warn
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
