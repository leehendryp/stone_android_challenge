package com.leehendryp.stoneandroidchallenge.feed.data.remote

import com.leehendryp.stoneandroidchallenge.core.extensions.getThrowable
import com.leehendryp.stoneandroidchallenge.feed.data.entities.JokeResponse
import com.leehendryp.stoneandroidchallenge.feed.data.entities.ResultResponse
import io.reactivex.rxjava3.core.Maybe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val api: JokesApi) :
    RemoteDataSource {
    companion object {
        private const val REMOTE_SOURCE_ERROR = "Could not fetch data from remote source"

        private class RemoteJokeListFetchException(message: String) : Throwable(message)
    }

    override fun search(query: String): Maybe<List<JokeResponse>> {
        return api.search(query)
            .flatMap { response ->
                if (response.isSuccessful) response.retrieveJokeList()
                else Maybe.error(response.getThrowable())
            }
    }

    private fun Response<ResultResponse>.retrieveJokeList(): Maybe<List<JokeResponse>>? {
        val jokeList = body()?.jokeList

        return if (jokeList != null) Maybe.just(jokeList)
        else Maybe.error(RemoteJokeListFetchException(REMOTE_SOURCE_ERROR)
        )
    }
}
