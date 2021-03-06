package com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel

import com.leehendryp.stoneandroidchallenge.feed.domain.model.Joke

sealed class FeedState {
    data class Default(var shouldWarnNoResult: Boolean = false) : FeedState()
    object Loading : FeedState()
    data class Success(val data: List<Joke>) : FeedState()
    data class Error(val error: Throwable) : FeedState()
}
