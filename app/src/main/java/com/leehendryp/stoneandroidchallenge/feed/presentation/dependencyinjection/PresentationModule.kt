package com.leehendryp.stoneandroidchallenge.feed.presentation.dependencyinjection

import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.disposables.CompositeDisposable

@Module
class PresentationModule {
    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()
}
