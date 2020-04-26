package com.leehendryp.stoneandroidchallenge.feed.data.remote

import com.leehendryp.stoneandroidchallenge.feed.data.entities.ResultResponse
import io.reactivex.rxjava3.core.Maybe
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface JokesApi {
    companion object {
        const val JOKES_API = "https://api.chucknorris.io/"
        private const val ENDPOINT = "jokes/search"
    }

    @POST(ENDPOINT)
    fun search(@Query("query") query: String): Maybe<Response<ResultResponse>>
}
