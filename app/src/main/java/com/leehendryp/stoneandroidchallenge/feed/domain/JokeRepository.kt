package com.leehendryp.stoneandroidchallenge.feed.domain

import com.leehendryp.stoneandroidchallenge.feed.domain.model.Joke
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

interface JokeRepository {
    fun search(query: String): Maybe<List<Joke>>
    fun save(joke: Joke): Completable
}