package com.leehendryp.stoneandroidchallenge.feed.data.remote

import com.leehendryp.stoneandroidchallenge.core.extensions.getThrowable
import com.leehendryp.stoneandroidchallenge.feed.data.entities.JokeResponse
import com.leehendryp.stoneandroidchallenge.feed.data.entities.ResultResponse
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject
import retrofit2.Response

class RemoteDataSourceImpl @Inject constructor(private val api: JokesApi) : RemoteDataSource {
    override fun search(query: String): Flowable<JokeResponse> {
        return api.search(query)
            .flattenAsFlowable { response ->
                if (response.isSuccessful) response.retrieveJokeList()
                else throw response.getThrowable()
            }
    }

    private fun Response<ResultResponse>.retrieveJokeList(): List<JokeResponse> {
        return body()?.jokeList ?: listOf()
    }
}
