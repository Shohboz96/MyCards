package com.example.examen.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.examen.data.CardData
import com.example.examen.room.dao.PersonDao

@Database(entities = [CardData::class], version = 1)
abstract class AppDataBase : RoomDatabase(){
    abstract fun personDao(): PersonDao

    companion object{
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            val tempInstance = INSTANCE
            if(tempInstance != null) return tempInstance
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                    AppDataBase::class.java,"app")
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}