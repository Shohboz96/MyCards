package com.example.examen.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.examen.data.CardData

@Dao
interface PersonDao : BaseDao<CardData> {

    @Query("select * from CardData")
    fun getAllFromRoom():List<CardData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: List<CardData>)

    @Query("delete from CardData")
    fun deleteAll()
}