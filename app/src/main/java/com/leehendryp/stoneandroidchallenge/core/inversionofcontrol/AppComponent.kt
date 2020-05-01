package com.leehendryp.stoneandroidchallenge.core.inversionofcontrol

import android.content.Context
import com.leehendryp.stoneandroidchallenge.feed.data.dependencyinjection.DataModule
import com.leehendryp.stoneandroidchallenge.feed.domain.dependencyinjection.DomainModule
import com.leehendryp.stoneandroidchallenge.feed.presentation.dependencyinjection.PresentationModule
import com.leehendryp.stoneandroidchallenge.feed.presentation.view.FeedFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DataModule::class,
        DomainModule::class,
        PresentationModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(fragment: FeedFragment)
}
