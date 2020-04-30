package com.leehendryp.stoneandroidchallenge.presentation

import com.leehendryp.stoneandroidchallenge.feed.domain.model.Joke

sealed class FeedState {
    object Default : FeedState()
    object Loading : FeedState()
    data class Success(val data: List<Joke>) : FeedState()
    data class Error(val error: Throwable) : FeedState()
}
