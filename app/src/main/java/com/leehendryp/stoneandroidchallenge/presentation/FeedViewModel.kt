package com.leehendryp.stoneandroidchallenge.presentation

import androidx.lifecycle.ViewModel
import com.leehendryp.stoneandroidchallenge.core.extensions.observeOnMain
import com.leehendryp.stoneandroidchallenge.core.extensions.subscribeOnIO
import com.leehendryp.stoneandroidchallenge.feed.domain.usecases.SearchJokeUseCase
import com.leehendryp.stoneandroidchallenge.presentation.FeedState.Default
import com.leehendryp.stoneandroidchallenge.presentation.FeedState.Error
import com.leehendryp.stoneandroidchallenge.presentation.FeedState.Loading
import com.leehendryp.stoneandroidchallenge.presentation.FeedState.Success
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class FeedViewModel @Inject constructor(
    private val searchJokeUseCase: SearchJokeUseCase
) : ViewModel() {
    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    val state by lazy { BehaviorSubject.create<FeedState>().apply { onNext(Default) } }

    fun search(query: String) {
        searchJokeUseCase.execute(query)
            .subscribeOnIO()
            .observeOnMain()
            .doOnSubscribe { state(Loading) }
            .doFinally { state(Default) }
            .subscribeBy(
                onSuccess = { jokeList -> state(Success(jokeList)) },
                onError = { error -> state(Error(error)) }
            )
    }

    private fun state(feedState: FeedState) = state.onNext(feedState)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}