package com.leehendryp.stoneandroidchallenge.feed.data.local

import com.leehendryp.stoneandroidchallenge.feed.domain.model.Joke
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface LocalDataSource {
    fun save(joke: Joke): Completable
    fun search(query: String): Flowable<Joke>
}
