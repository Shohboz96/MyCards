package com.example.examen.room.dao

import androidx.room.*

@Dao
interface BaseDao <T>{
    @Delete
    fun deleteFromRoom(data:T)

    @Update
    fun updateToRoom(data: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertToRoom(data: T)

}