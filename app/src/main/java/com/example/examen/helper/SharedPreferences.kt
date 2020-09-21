package com.example.examen.helper

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences (val context: Context){
   private val pref: SharedPreferences = context.getSharedPreferences("into",Context.MODE_PRIVATE)

    fun setUserToken(token:String){
        pref.edit().apply(){
            putString("key",token)
            apply()
        }
    }
    fun getUserToken():String{
        return pref.getString("key","")!!
    }
    fun setString(key:String,str:String){
        pref.edit().apply(){
            putString(key,str)
            apply()
        }
    }
    fun getString(key:String):String{
       return pref.getString(key,"")!!
    }

}