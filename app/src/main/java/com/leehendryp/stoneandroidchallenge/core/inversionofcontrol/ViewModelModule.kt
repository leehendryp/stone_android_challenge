package com.leehendryp.stoneandroidchallenge.core.inversionofcontrol

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.leehendryp.stoneandroidchallenge.feed.presentation.viewmodel.FeedViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FeedViewModel::class)
    abstract fun bindFeedViewModel(viewModel: FeedViewModel): ViewModel
}
