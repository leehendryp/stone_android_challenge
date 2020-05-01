package com.leehendryp.stoneandroidchallenge.feed.data.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.leehendryp.stoneandroidchallenge.core.DTOs
import com.leehendryp.stoneandroidchallenge.core.MyBadException
import com.leehendryp.stoneandroidchallenge.core.ResponseType.SUCCESS
import com.leehendryp.stoneandroidchallenge.core.RxUnitTest
import com.leehendryp.stoneandroidchallenge.core.ServiceInstabilityException
import com.leehendryp.stoneandroidchallenge.core.createRetrofitInstance
import com.leehendryp.stoneandroidchallenge.core.setResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RemoteDataSourceImplTest : RxUnitTest() {
    companion object {
        private const val JOKE_LIST_RESPONSE = "JokeListResponse.json"
    }

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var apiServer: MockWebServer
    private lateinit var api: JokesApi

    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun `set up`() {
        apiServer = MockWebServer()
        initApiFromMockedServer()
        remoteDataSource = RemoteDataSourceImpl(api)
    }

    private fun initApiFromMockedServer() {
        val retrofit = apiServer.createRetrofitInstance()
        api = retrofit.create(JokesApi::class.java)
    }

    @Test
    fun `should emit the right list of joke responses upon successful data fetch request to API`() {
        apiServer.setResponse(SUCCESS, JOKE_LIST_RESPONSE)

        remoteDataSource.search("")
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertResult(DTOs.correctJokeResponses)
    }

    @Test
    fun `should emit MyBadException upon improper request to API`() {
        apiServer.setResponse(400)

        remoteDataSource.search("")
            .test()
            .assertNotComplete()
            .assertError { it is MyBadException }
    }

    @Test
    fun `should emit ServiceInstabilityException upon API instability`() {
        apiServer.setResponse(500)

        remoteDataSource.search("")
            .test()
            .assertNotComplete()
            .assertError { it is ServiceInstabilityException }
    }

    @After
    fun `shut down`() {
        apiServer.shutdown()
    }
}
