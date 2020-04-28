package com.leehendryp.stoneandroidchallenge.feed.data.local.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class TypeConverter {
    @TypeConverter
    fun fromStringList(stringList: List<String>): String = Gson().toJson(stringList)

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}
            .type as Type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromRoomJoke(roomJoke: RoomJoke): String = Gson().toJson(roomJoke)

    @TypeConverter
    fun toRoomJoke(value: String): RoomJoke {
        val listType = object : TypeToken<RoomJoke>() {}
            .type as Type
        return Gson().fromJson(value, listType)
    }
}
