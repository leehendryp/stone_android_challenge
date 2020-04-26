package com.leehendryp.stoneandroidchallenge.core.extensions

import com.leehendryp.stoneandroidchallenge.core.BadRequestException
import com.leehendryp.stoneandroidchallenge.core.NotFoundException
import com.leehendryp.stoneandroidchallenge.core.RequestTimeoutException
import com.leehendryp.stoneandroidchallenge.core.UnauthorizedException
import retrofit2.Response

fun Response<*>.getThrowable(): Throwable {
    return when (code()) {
        400 -> BadRequestException(
            message() ?: ""
        )
        401 -> UnauthorizedException(
            message() ?: ""
        )
        404 -> NotFoundException(
            message() ?: ""
        )
        408 -> RequestTimeoutException(
            message() ?: ""
        )
        else -> Throwable(message() ?: "")
    }
}
