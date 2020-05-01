package com.leehendryp.stoneandroidchallenge.core.extensions

import com.leehendryp.stoneandroidchallenge.core.MyBadException
import com.leehendryp.stoneandroidchallenge.core.ServiceInstabilityException
import retrofit2.Response

fun Response<*>.getThrowable(): Throwable {
    return with(code()) {
        when {
            this in 400..499 -> MyBadException(message() ?: "")
            this >= 500 -> ServiceInstabilityException(message() ?: "")
            else -> Throwable(message() ?: "")
        }
    }
}
