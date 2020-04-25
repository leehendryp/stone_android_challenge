package com.leehendryp.stoneandroidchallenge.core

import com.leehendryp.stoneandroidchallenge.core.ResponseType.CLIENT_ERROR
import com.leehendryp.stoneandroidchallenge.core.ResponseType.REDIRECT
import com.leehendryp.stoneandroidchallenge.core.ResponseType.SERVER_ERROR
import com.leehendryp.stoneandroidchallenge.core.ResponseType.SUCCESS
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun MockWebServer.createRetrofitInstance(): Retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .client(OkHttpClient.Builder().build())
    .baseUrl(this.url("/").toString())
    .build()

fun MockWebServer.setResponse(type: ResponseType) {
    when (type) {
        SUCCESS -> setResponse(200)
        REDIRECT -> setResponse(300)
        CLIENT_ERROR -> setResponse(400)
        SERVER_ERROR -> setResponse(500)
    }
}

private fun MockWebServer.setResponse(httpCode: Int) = with(MockResponse()) {
    setResponseCode(httpCode)
    setResponseBodyFrom(httpCode)
    enqueue(this)
}

fun MockWebServer.setResponse(type: ResponseType, jsonFileName: String) = with(MockResponse()) {
    setResponseCodeFrom(type)
    setBody(getStringJson(jsonFileName))
    enqueue(this)
}

private fun MockResponse.setResponseCodeFrom(type: ResponseType) {
    setResponseCode(
        when (type) {
            SUCCESS -> 200
            REDIRECT -> 300
            CLIENT_ERROR -> 400
            SERVER_ERROR -> 500
        }
    )
}

private fun MockResponse.setResponseBodyFrom(httpCode: Int) {
    setBody(if (httpCode >= 400 || httpCode == 204) "" else "{}")
}

enum class ResponseType {
    SUCCESS,
    REDIRECT,
    CLIENT_ERROR,
    SERVER_ERROR
}