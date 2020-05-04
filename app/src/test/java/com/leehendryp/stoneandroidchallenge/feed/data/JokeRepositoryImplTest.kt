package com.leehendryp.stoneandroidchallenge.feed.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.leehendryp.stoneandroidchallenge.core.DTOs
import com.leehendryp.stoneandroidchallenge.core.RxUnitTest
import com.leehendryp.stoneandroidchallenge.core.utils.NetworkUtils
import com.leehendryp.stoneandroidchallenge.feed.data.local.LocalDataSource
import com.leehendryp.stoneandroidchallenge.feed.data.remote.RemoteDataSource
import com.leehendryp.stoneandroidchallenge.feed.domain.JokeRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class JokeRepositoryImplTest : RxUnitTest() {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var networkUtils: NetworkUtils
    private lateinit var remoteSource: RemoteDataSource
    private lateinit var localSource: LocalDataSource

    private lateinit var repo: JokeRepository

    @Before
    fun `set up`() {
        networkUtils = mockk()
        remoteSource = spyk()
        localSource = spyk()
        repo = JokeRepositoryImpl(networkUtils, remoteSource, localSource)
    }

    @Test
    fun `should fetch data from remote source if network is available`() {
        every { networkUtils.isInternetAvailable() } returns true

        every { remoteSource.search(any()) } returns DTOs.jokeResponseFlowable

        repo.search("")
            .test()
            .assertComplete()
            .assertNoErrors()
            .values()
            .containsAll(DTOs.jokes)

        verify(exactly = 1) { remoteSource.search(any()) }
        verify(exactly = 0) { localSource.search(any()) }
    }

    @Test
    fun `should fetch data from local source if network is unavailable`() {
        every { networkUtils.isInternetAvailable() } returns false

        every { localSource.search(any()) } returns DTOs.jokeFlowable

        repo.search("")
            .test()
            .assertComplete()
            .assertNoErrors()
            .values()
            .containsAll(DTOs.jokes)

        verify(exactly = 0) { remoteSource.search(any()) }
        verify(exactly = 1) { localSource.search(any()) }
    }

    @Test
    fun `should save data locally after remote data fetch if network is available`() {
        every { networkUtils.isInternetAvailable() } returns true

        every { remoteSource.search(any()) } returns DTOs.jokeResponseFlowable

        repo.search("")
            .test()
            .assertComplete()
            .assertNoErrors()

        verifyOrder {
            remoteSource.search(any())
            localSource.save(any())
        }

        verify(exactly = 0) { localSource.search(any()) }
    }
}
