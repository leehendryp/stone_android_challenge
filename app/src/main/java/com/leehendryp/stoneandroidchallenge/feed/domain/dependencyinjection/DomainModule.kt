package com.leehendryp.stoneandroidchallenge.feed.domain.dependencyinjection

import com.leehendryp.stoneandroidchallenge.feed.domain.JokeRepository
import com.leehendryp.stoneandroidchallenge.feed.domain.usecases.SearchJokeUseCase
import com.leehendryp.stoneandroidchallenge.feed.domain.usecases.SearchJokeUseCaseImpl
import dagger.Module
import dagger.Provides

@Module
class DomainModule {
    @Provides
    fun provideSearchUseCase(repository: JokeRepository): SearchJokeUseCase =
        SearchJokeUseCaseImpl(repository)
}
