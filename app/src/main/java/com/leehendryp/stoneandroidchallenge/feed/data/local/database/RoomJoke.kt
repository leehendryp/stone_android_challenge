package com.leehendryp.stoneandroidchallenge.feed.data.local.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomJoke(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @NonNull @ColumnInfo(name = "categories") val categories: List<String>,
    @NonNull @ColumnInfo(name = "updated_at") val updatedAt: String,
    @NonNull @ColumnInfo(name = "value") val value: String
)
