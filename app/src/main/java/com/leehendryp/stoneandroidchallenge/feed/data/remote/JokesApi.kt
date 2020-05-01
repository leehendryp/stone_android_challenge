package com.leehendryp.stoneandroidchallenge.feed.data.remote

import com.leehendryp.stoneandroidchallenge.feed.data.entities.ResultResponse
import io.reactivex.rxjava3.core.Maybe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface JokesApi {
    companion object {
        const val BASE_URL = "https://api.chucknorris.io/"
        private const val ENDPOINT = "jokes/search"
    }

    @GET(ENDPOINT)
    fun search(@Query("query") query: String): Maybe<Response<ResultResponse>>
}
