package com.leehendryp.stoneandroidchallenge.feed.domain.usecases

import com.leehendryp.stoneandroidchallenge.feed.domain.model.Joke
import io.reactivex.rxjava3.core.Maybe

interface SearchJokeUseCase {
    fun execute(query: String): Maybe<List<Joke>>
}