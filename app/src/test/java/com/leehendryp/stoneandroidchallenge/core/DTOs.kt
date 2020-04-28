package com.leehendryp.stoneandroidchallenge.core

import com.leehendryp.stoneandroidchallenge.feed.data.entities.JokeResponse
import com.leehendryp.stoneandroidchallenge.feed.data.entities.ResultResponse

object DTOs {
    val jokeOneResponse = JokeResponse(
        categories = listOf(),
        createdAt = "2020-01-05 13:42:20.262289",
        iconUrl = "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
        id = "56lSE2p9TlmrmfnjuwRSHw",
        updatedAt = "2020-01-05 13:42:20.262289",
        url = "https://api.chucknorris.io/jokes/56lSE2p9TlmrmfnjuwRSHw",
        value = "Chuck Norris once got a 500 in bowling. Without a ball. Or pins. Or oxygen."
    )

    val jokeTwoResponse = JokeResponse(
        categories = listOf(),
        createdAt = "2020-01-05 13:42:21.795084",
        iconUrl = "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
        id = "_0cHgTLsS2-NMZak9BYYrw",
        updatedAt = "2020-01-05 13:42:21.795084",
        url = "https://api.chucknorris.io/jokes/_0cHgTLsS2-NMZak9BYYrw",
        value = "Oxygen requires Chuck Norris to live"
    )

    val jokeThreeResponse = JokeResponse(
        categories = listOf(),
        createdAt = "2020-01-05 13:42:24.142371",
        iconUrl = "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
        id = "z4gAnDQyRdO7KfrZzDfLVA",
        updatedAt = "2020-01-05 13:42:24.142371",
        url = "https://api.chucknorris.io/jokes/z4gAnDQyRdO7KfrZzDfLVA",
        value = "Chuck Norris once got a 500 game in bowling. Without a ball. Or pins. Or oxygen. And nobody has ever worked up the courage to ask him how."
    )

    val correctJokeResponses = listOf(
        jokeOneResponse,
        jokeTwoResponse,
        jokeThreeResponse
    )

    val incorrectJokeResponses: List<JokeResponse> = listOf()

    val correctResultResponse = ResultResponse(
        jokeList = correctJokeResponses,
        total = correctJokeResponses.size
    )

    val incorrectResultResponse = ResultResponse(
        jokeList = incorrectJokeResponses,
        total = incorrectJokeResponses.size
    )
}
