package com.leehendryp.stoneandroidchallenge.feed.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.leehendryp.stoneandroidchallenge.core.DTOs
import com.leehendryp.stoneandroidchallenge.core.RxUnitTest
import com.leehendryp.stoneandroidchallenge.feed.data.local.database.RoomJokeDao
import com.leehendryp.stoneandroidchallenge.feed.data.toJoke
import io.mockk.every
import io.mockk.spyk
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LocalDataSourceImplTest : RxUnitTest() {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var dao: RoomJokeDao
    private lateinit var localSource: LocalDataSource

    @Before
    fun `set up`() {
        dao = spyk()
        localSource = LocalDataSourceImpl(dao)
    }

    @Test
    fun `should emit complete upon successful attempt to save joke in database`() {
        every { dao.insert(any()) } returns Completable.complete()

        localSource.save(DTOs.jokeOneResponse.toJoke())
            .test()
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun `should emit error upon failed attempt to save joke in database`() {
        val error = Throwable()

        every { dao.insert(any()) } returns Completable.error(error)

        localSource.save(DTOs.jokeOneResponse.toJoke())
            .test()
            .assertNotComplete()
            .assertError(error)
    }

    @Test
    fun `should emit jokes upon successful search in database`() {
        every { dao.search(any()) } returns DTOs.roomJokeFlowable

        localSource.search("")
            .test()
            .assertComplete()
            .assertNoErrors()
            .values()
            .containsAll(DTOs.jokes)
    }

    @Test
    fun `should emit no joke upon successful no-result search in database`() {
        every { dao.search(any()) } returns Flowable.empty()

        localSource.search("")
            .test()
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun `should emit error upon failed search attempt in database`() {
        val error = Throwable()

        every { dao.search(any()) } returns Flowable.error(error)

        localSource.search("")
            .test()
            .assertNotComplete()
            .assertError(error)
    }
}
