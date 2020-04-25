package com.leehendryp.stoneandroidchallenge.core

import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.Rule
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

abstract class RxUnitTest {
    private val testScheduler = TestScheduler()

    init {
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
    }

    @Rule @JvmField
    val rule: MockitoRule = MockitoJUnit.rule()

    @Rule
    @JvmField var testSchedulerRule = RxImmediateSchedulerRule()
}
