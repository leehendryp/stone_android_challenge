package com.leehendryp.stoneandroidchallenge.feed.domain.model

import com.google.gson.annotations.SerializedName

data class Joke(
    @SerializedName("categories") val categories: List<String>,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("value") val value: String
)