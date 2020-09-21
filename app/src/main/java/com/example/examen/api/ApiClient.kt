package com.example.examen.api

import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.examen.app.App
import com.example.examen.helper.SharedPreferences

object ApiClient{

    private val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    private val pref = SharedPreferences(App.instance)

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(ChuckInterceptor(App.instance))
        .addInterceptor {
            val requestOld = it.request()
            val request = requestOld.newBuilder()
                .removeHeader("token")
                .addHeader("token", pref.getUserToken())
                .method(requestOld.method, requestOld.body)
                .build()
            val response = it.proceed(request)
            response
        }
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://161.35.73.101:99/")
      //  .baseUrl("https://2b5ad90acf1d.ngrok.io/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //http://161.35.73.101:99
}