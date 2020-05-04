package com.leehendryp.stoneandroidchallenge.feed.data.remote

import com.leehendryp.stoneandroidchallenge.feed.data.entities.JokeResponse
import io.reactivex.rxjava3.core.Flowable

interface RemoteDataSource {
    fun search(query: String): Flowable<JokeResponse>
}
