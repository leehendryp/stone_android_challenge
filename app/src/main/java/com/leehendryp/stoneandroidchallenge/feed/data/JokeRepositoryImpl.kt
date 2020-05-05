package com.leehendryp.stoneandroidchallenge.feed.data

import com.leehendryp.stoneandroidchallenge.core.utils.NetworkUtils
import com.leehendryp.stoneandroidchallenge.feed.data.local.LocalDataSource
import com.leehendryp.stoneandroidchallenge.feed.data.remote.RemoteDataSource
import com.leehendryp.stoneandroidchallenge.feed.domain.JokeRepository
import com.leehendryp.stoneandroidchallenge.feed.domain.model.Joke
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class JokeRepositoryImpl @Inject constructor(
    private val networkUtils: NetworkUtils,
    private val remoteSource: RemoteDataSource,
    private val localSource: LocalDataSource
) : JokeRepository {
    companion object {
        private const val LOCAL_TIMEOUT: Long = 10
    }
    override fun search(query: String): Flowable<Joke> {
        return if (networkUtils.isInternetAvailable()) remoteSearch(query)
            .doOnNext { save(it) }
        else localSearch(query)
    }

    override fun save(joke: Joke): Completable = localSource.save(joke)

    private fun remoteSearch(query: String): Flowable<Joke> = remoteSource.search(query)
        .map { response -> response.toJoke() }

    private fun localSearch(query: String): Flowable<Joke> = localSource.search(query)
        .timeout(LOCAL_TIMEOUT, TimeUnit.SECONDS)
}
