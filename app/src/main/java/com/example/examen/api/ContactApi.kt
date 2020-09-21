package com.example.examen.api

import com.example.examen.data.*
import retrofit2.Call
import retrofit2.http.*

interface ContactApi {
    /**
     * 1. Get all contacts
     * */
    @GET("card")
//    @Headers("lang:uz")
    fun getAll(): Call<ResponseData<List<CardData>>>

    /**
     * 2. add new a contact
     * */
    @POST("contact/register")
    fun addUser(@Body contactData: ContactUserData): Call<ResponseData<ContactUserData>>

    @POST("card")
    fun addCard(@Body contactData: AddCardData): Call<ResponseData<CardData>>

    @POST("contact")
    fun add(@Body contactData: CardData): Call<ResponseData<CardData>>

    @POST("contact/verify")
    fun verify(@Body verify: SmsCodeData): Call<ResponseData<String>>

    @POST("card/verify")
    fun verifyCard(@Body verify: VerifyCardData): Call<ResponseData<Any>>

    @POST("contact/login")
    fun login(@Body login: LoginData): Call<ResponseData<String>>

    @POST("contact/reset")
    fun reset(@Body number: ResetData): Call<ResponseData<String>>

    @POST("contact/password")
    fun password(@Body password: PasswordData): Call<ResponseData<Any>>

    /**
     * 3. remove a contact
     * */
    @HTTP(method = "DELETE", path = "contact", hasBody = true)
    fun remove(@Body contactData: CardData): Call<ResponseData<CardData>>

    /**
     * Update
     */
    @PUT("contact")
    fun update(@Body contactData: CardData):Call<ResponseData<CardData>>
}