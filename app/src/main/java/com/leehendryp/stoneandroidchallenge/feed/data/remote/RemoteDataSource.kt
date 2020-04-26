package com.leehendryp.stoneandroidchallenge.feed.data.remote

import com.leehendryp.stoneandroidchallenge.feed.data.entities.JokeResponse
import io.reactivex.rxjava3.core.Maybe

interface RemoteDataSource {
    fun search(query: String): Maybe<List<JokeResponse>>
}
