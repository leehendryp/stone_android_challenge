package com.leehendryp.stoneandroidchallenge.feed.data.local

import com.leehendryp.stoneandroidchallenge.core.extensions.asV3Completable
import com.leehendryp.stoneandroidchallenge.core.extensions.asV3Flowable
import com.leehendryp.stoneandroidchallenge.core.extensions.subscribeOnIO
import com.leehendryp.stoneandroidchallenge.feed.data.local.database.RoomJokeDao
import com.leehendryp.stoneandroidchallenge.feed.data.toJoke
import com.leehendryp.stoneandroidchallenge.feed.data.toRoomJoke
import com.leehendryp.stoneandroidchallenge.feed.domain.model.Joke
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

// FIXME Lee April 27, 2020: Unfortunately, Room does not support RxJava3 as of now
// This project uses Akarnokd's bridge lib to work around the issue. Akarnokd is one
// of the people behind RxJava.

class LocalDataSourceImpl @Inject constructor(private val dao: RoomJokeDao) : LocalDataSource {
    override fun save(joke: Joke): Completable {
        return dao.insert(joke.toRoomJoke())
            .asV3Completable()
            .subscribeOnIO()
    }

    override fun search(query: String): Flowable<Joke> {
        return dao.search(query)
            .asV3Flowable()
            .subscribeOnIO()
            .map { it.toJoke() }
    }
}
