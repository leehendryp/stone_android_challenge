package com.leehendryp.stoneandroidchallenge.feed.domain.usecases

import com.leehendryp.stoneandroidchallenge.feed.domain.JokeRepository
import com.leehendryp.stoneandroidchallenge.feed.domain.model.Joke
import io.reactivex.rxjava3.core.Maybe
import javax.inject.Inject

class SearchJokeUseCaseImpl @Inject constructor(
    private val repository: JokeRepository
) : SearchJokeUseCase {
    override fun execute(query: String): Maybe<List<Joke>> = repository.search(query)
}
