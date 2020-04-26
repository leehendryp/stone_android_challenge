package com.leehendryp.stoneandroidchallenge.feed.data.entities

import com.google.gson.annotations.SerializedName

data class ResultResponse(
    @SerializedName("result") val jokeList: List<JokeResponse>?,
    @SerializedName("total") val total: Int?
)
