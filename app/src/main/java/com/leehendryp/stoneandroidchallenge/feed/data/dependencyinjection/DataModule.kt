package com.leehendryp.stoneandroidchallenge.feed.data.dependencyinjection

import android.content.Context
import com.leehendryp.stoneandroidchallenge.core.utils.NetworkUtils
import com.leehendryp.stoneandroidchallenge.feed.data.JokeRepositoryImpl
import com.leehendryp.stoneandroidchallenge.feed.data.local.LocalDataSource
import com.leehendryp.stoneandroidchallenge.feed.data.local.LocalDataSourceImpl
import com.leehendryp.stoneandroidchallenge.feed.data.local.database.JokeDatabase
import com.leehendryp.stoneandroidchallenge.feed.data.local.database.JokeDatabase.Companion.getDatabase
import com.leehendryp.stoneandroidchallenge.feed.data.local.database.RoomJokeDao
import com.leehendryp.stoneandroidchallenge.feed.data.remote.JokesApi
import com.leehendryp.stoneandroidchallenge.feed.data.remote.RemoteDataSource
import com.leehendryp.stoneandroidchallenge.feed.data.remote.RemoteDataSourceImpl
import com.leehendryp.stoneandroidchallenge.feed.domain.JokeRepository
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class DataModule {
    @Provides
    fun provideHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()

    @Singleton
    @Provides
    fun provideNetworkService(okHttpClient: OkHttpClient): JokesApi = Retrofit.Builder()
        .baseUrl(JokesApi.BASE_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(JokesApi::class.java)

    @Singleton
    @Provides
    fun provideDatabase(context: Context): JokeDatabase = getDatabase(context)

    @Singleton
    @Provides
    fun prdovideDao(database: JokeDatabase): RoomJokeDao = database.roomJokeDao()

    @Provides
    fun provideRemoteDataSource(api: JokesApi): RemoteDataSource = RemoteDataSourceImpl(api)

    @Provides
    fun provideLocalDataSource(dao: RoomJokeDao): LocalDataSource = LocalDataSourceImpl(dao)

    @Provides
    fun provideNetworkUtils(context: Context): NetworkUtils = NetworkUtils(context)

    @Singleton
    @Provides
    fun provideRepository(
        networkUtils: NetworkUtils,
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): JokeRepository = JokeRepositoryImpl(networkUtils, remoteDataSource, localDataSource)
}
