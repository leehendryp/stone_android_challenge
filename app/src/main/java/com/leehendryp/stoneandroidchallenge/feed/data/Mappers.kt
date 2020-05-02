package com.leehendryp.stoneandroidchallenge.feed.data

import com.leehendryp.stoneandroidchallenge.feed.data.entities.JokeResponse
import com.leehendryp.stoneandroidchallenge.feed.data.local.database.RoomJoke
import com.leehendryp.stoneandroidchallenge.feed.domain.model.Joke

fun List<JokeResponse>.toJokeList(): List<Joke> {
    val jokeList = mutableListOf<Joke>()
    forEach { response -> jokeList.add(response.toJoke()) }
    return jokeList
}

fun JokeResponse.toJoke() = Joke(
    categories = categories ?: listOf(),
    updatedAt = updatedAt ?: "",
    url = url ?: "",
    value = value ?: ""
)

fun RoomJoke.toJoke() = Joke(
    categories = categories,
    updatedAt = updatedAt,
    url = url,
    value = value
)

fun Joke.toRoomJoke() = RoomJoke(
    categories = categories,
    updatedAt = updatedAt,
    url = url,
    value = value
)
