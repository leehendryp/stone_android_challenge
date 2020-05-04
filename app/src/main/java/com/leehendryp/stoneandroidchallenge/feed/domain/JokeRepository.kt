package com.leehendryp.stoneandroidchallenge.feed.domain

import com.leehendryp.stoneandroidchallenge.feed.domain.model.Joke
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface JokeRepository {
    fun search(query: String): Flowable<Joke>
    fun save(joke: Joke): Completable
}
