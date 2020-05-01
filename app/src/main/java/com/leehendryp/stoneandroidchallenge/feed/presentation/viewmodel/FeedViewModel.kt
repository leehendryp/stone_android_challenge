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

    val state: BehaviorSubject<FeedState> by lazy {
        BehaviorSubject.create<FeedState>()
            .apply { onNext(Default) }
    }

    fun search(query: String) {
        compositeDisposable.add(
            searchJokeUseCase.execute(query)
                .subscribeOnIO()
                .observeOnMain()
                .doOnSubscribe { state(Loading) }
                .doFinally { state(Default) }
                .subscribeBy(
                    onSuccess = { jokeList -> state(Success(jokeList)) },
                    onError = { error -> state(Error(error)) }
                )
        )
    }

    private fun state(feedState: FeedState) = state.onNext(feedState)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
