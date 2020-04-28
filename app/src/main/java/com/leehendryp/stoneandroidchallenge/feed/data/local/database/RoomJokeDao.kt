package com.leehendryp.stoneandroidchallenge.feed.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface RoomJokeDao {
    @Query("Select * from roomjoke where value like  :query")
    fun search(query: String): Flowable<RoomJoke>

    @Query("SELECT * FROM roomjoke")
    fun getAll(): Flowable<RoomJoke>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(roomJoke: RoomJoke): Completable
}
