package com.leehendryp.stoneandroidchallenge.feed.domain.usecases

import com.leehendryp.stoneandroidchallenge.feed.domain.model.Joke
import io.reactivex.rxjava3.core.Flowable

interface SearchJokeUseCase {
    fun execute(query: String): Flowable<Joke>
}
