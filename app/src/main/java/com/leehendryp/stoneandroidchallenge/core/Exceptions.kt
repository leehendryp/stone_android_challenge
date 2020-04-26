package com.leehendryp.stoneandroidchallenge.core

class BadRequestException(message: String) : Throwable(message)
class UnauthorizedException(message: String) : Throwable(message)
class NotFoundException(message: String) : Throwable(message)
class RequestTimeoutException(message: String) : Throwable(message)
