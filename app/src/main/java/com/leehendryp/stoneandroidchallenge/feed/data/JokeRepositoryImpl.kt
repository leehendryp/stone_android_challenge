package com.leehendryp.stoneandroidchallenge.feed.data

import com.leehendryp.stoneandroidchallenge.core.utils.NetworkUtils
import com.leehendryp.stoneandroidchallenge.feed.data.local.LocalDataSource
import com.leehendryp.stoneandroidchallenge.feed.data.remote.RemoteDataSource
import com.leehendryp.stoneandroidchallenge.feed.domain.JokeRepository
import com.leehendryp.stoneandroidchallenge.feed.domain.model.Joke
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import javax.inject.Inject

class JokeRepositoryImpl @Inject constructor(
    private val networkUtils: NetworkUtils,
    private val remoteSource: RemoteDataSource,
    private val localSource: LocalDataSource
) : JokeRepository {
    companion object {
        private const val BUFFER_CAPACITY = 20
    }

    override fun search(query: String): Maybe<List<Joke>> {
        return if (networkUtils.isInternetAvailable()) remoteSearch(query)
            .doAfterSuccess { response -> response.forEach { save(it) } }
        else localSearch(query)
    }

    override fun save(joke: Joke): Completable = localSource.save(joke)

    private fun remoteSearch(query: String): Maybe<List<Joke>> = remoteSource.search(query)
        .flatMap { response -> Maybe.just(response.toJokeList()) }

    private fun localSearch(query: String): Maybe<List<Joke>> {
        return localSource.search(query)
            .onBackpressureBuffer(BUFFER_CAPACITY)
            .toList()
            .flatMapMaybe { Maybe.just(it) }
    }
}
