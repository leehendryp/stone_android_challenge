package com.leehendryp.stoneandroidchallenge.feed.data

import com.leehendryp.stoneandroidchallenge.feed.data.entities.JokeResponse
import com.leehendryp.stoneandroidchallenge.feed.domain.model.Joke

fun List<JokeResponse>.toJokeList(): List<Joke> {
    val jokeList = mutableListOf<Joke>()
    forEach { response -> jokeList.add(response.toJoke()) }
    return jokeList
}

fun JokeResponse.toJoke() = Joke(
    categories = categories ?: listOf(),
    updatedAt = updatedAt ?: "",
    value = value ?: ""
)
