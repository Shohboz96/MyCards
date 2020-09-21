package com.example.examen.mvp

import android.util.Log
import com.example.examen.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.examen.app.App
import com.example.examen.api.ContactApi
import com.example.examen.api.ResponseData
import com.example.examen.data.CardData
import com.example.examen.utils.ResultData
import com.example.examen.room.AppDataBase
import java.util.concurrent.Executors

class ContactRepository(private val contactApi: ContactApi) : ContactContract.Model {
    private val executor = Executors.newSingleThreadExecutor()
    private val room = AppDataBase.getInstance(App.instance).personDao()

    override fun insertAll(list: List<CardData>) {
        executor.execute {
            room.deleteAll()
            room.insertAll(list)
            Log.d("AAA", "inserted-${list.size}")
        }
    }

    override fun deleteFromRoom(data: CardData) {
        executor.execute {
            room.deleteFromRoom(data)
        }
    }

    override fun insertToRoom(data: CardData) {
        executor.execute {
            room.insertToRoom(data)
        }

    }

    override fun updateInRoom(data: CardData) {
        executor.execute {
            room.updateToRoom(data)
        }
    }

    override fun deleteAll() {
        executor.execute {
            room.deleteAll()
        }
    }

    override fun add(contactData: CardData, block: (ResultData<CardData>) -> Unit) {
        contactApi.add(contactData).enqueue(object : Callback<ResponseData<CardData>>{
            override fun onFailure(call: Call<ResponseData<CardData>>, t: Throwable) {
                block(ResultData.failure(t))
            }

            override fun onResponse(call: Call<ResponseData<CardData>>, response: Response<ResponseData<CardData>>
            ) {
                val res = response.body()
                when{
                    res == null -> block(ResultData.resource(R.string.empty_body))
                    res.status == "ERROR" -> block(ResultData.message(res.message))
                    res.status == "OK" -> block(ResultData.data(res.data ?: contactData))
                }
            }
        })

       /* executor.execute {
            try {
                val response = contactApi.add(contactData).execute().body()
                when {
                    response == null -> block(ResultData.resource(R.string.empty_body))
                    response.status == "ERROR" -> block(ResultData.message(response.message))
                    response.status == "OK" -> block(ResultData.data(response.data ?: contactData))
                }
            } catch (e: Throwable) {

            }
        }*/
    }

    override fun remove(contactData: CardData, block: (ResultData<CardData>) -> Unit) {
        contactApi.remove(contactData).enqueue(object : Callback<ResponseData<CardData>>{
            override fun onFailure(call: Call<ResponseData<CardData>>, t: Throwable) {
                block(ResultData.failure(t))
            }

            override fun onResponse(call: Call<ResponseData<CardData>>, response: Response<ResponseData<CardData>>
            ) {
                val res = response.body()
                when{
                    res == null -> block(ResultData.resource(R.string.empty_body))
                    res.status == "ERROR" -> block(ResultData.message(res.message))
                    res.status == "OK" -> block(ResultData.data(res.data ?: contactData))
                }
            }
        })
        /*executor.execute {
            try {
                val response = contactApi.remove(contactData).execute().body()
                when {
                    response == null -> block(ResultData.resource(R.string.empty_body))
                    response.status == "ERROR" -> block(ResultData.message(response.message))
                    response.status == "OK" -> block(ResultData.data(response.data ?: contactData))
                }

            } catch (e: Throwable) {

            }
        }*/
    }

    override fun getAll(block: (ResultData<List<CardData>>) -> Unit) {
        /*  try {
          val response = contactApi.getAll().execute().body()
              when{
                  response == null -> block(ResultData.resource(R.string.empty_body))
                  response.status == "ERROR" -> block(ResultData.message(response.message))
                  response.status == "OK" -> block(ResultData.data(response.data ?: emptyList()))
              }
          } catch (e:Throwable){

          }*/
        contactApi.getAll().enqueue(object : Callback<ResponseData<List<CardData>>> {

            override fun onFailure(call: Call<ResponseData<List<CardData>>>, t: Throwable) {
                block(ResultData.failure(t))
            }

            override fun onResponse(
                call: Call<ResponseData<List<CardData>>>,
                response: Response<ResponseData<List<CardData>>>
            ) {
                val res = response.body()
                when {
                    res == null -> block(ResultData.resource(R.string.empty_body))
                    res.status == "ERROR" -> block(ResultData.message(res.message))
                    res.status == "OK" -> {
                        block(ResultData.data(res.data ?: emptyList()))
                        //       room.insertAll(res.data ?: emptyList())
                    }
                }
            }
        })
    }

    override fun getAllFromRoom(): List<CardData> {
        val ls  = ArrayList<CardData>()
        executor.execute {
            ls.addAll(room.getAllFromRoom())
        }
        return ls
    }

    override fun edit(contactData: CardData, block: (ResultData<CardData>) -> Unit) {
        contactApi.update(contactData).enqueue(object : Callback<ResponseData<CardData>>{
            override fun onFailure(call: Call<ResponseData<CardData>>, t: Throwable) {
                block(ResultData.failure(t))
            }

            override fun onResponse(call: Call<ResponseData<CardData>>, response: Response<ResponseData<CardData>>
            ) {
                val res = response.body()
                when{
                    res == null -> block(ResultData.resource(R.string.empty_body))
                    res.status == "ERROR" -> block(ResultData.message(res.message))
                    res.status == "OK" -> block(ResultData.data(res.data ?: contactData))
                }
            }
        })

       /* executor.execute {
            try {
                val rs = contactApi.update(contactData).execute().body()
                when {
                    rs == null -> block(ResultData.resource(R.string.empty_body))
                    rs.status == "ERROR" -> block(ResultData.message(rs.message))
                    rs.status == "OK" -> block(ResultData.data(rs.data ?: contactData))
                }
            } catch (e: Throwable) {

            }
        }*/

    }
}