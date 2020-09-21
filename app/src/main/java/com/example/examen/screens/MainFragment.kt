package com.example.examen.screens

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.examen.R
import com.example.examen.adapter.CardAdapter
import kotlinx.android.synthetic.main.fragment_main.*
import com.example.examen.api.ApiClient
import com.example.examen.api.ContactApi
import com.example.examen.app.App
import com.example.examen.data.CardData
import com.example.examen.helper.SharedPreferences
import com.example.examen.room.AppDataBase
import com.example.examen.screensCard.AddCardFragment
import com.example.examen.utils.extensions.changeNavigationBarColor
import com.example.examen.utils.extensions.changeStatusBarColor
import retrofit2.HttpException
import java.util.concurrent.Executors


class MainFragment : Fragment(R.layout.fragment_main){

    private lateinit var adapter : CardAdapter
    private val executor = Executors.newSingleThreadExecutor()
    private val room = AppDataBase.getInstance(App.instance).personDao()
    private val api = ApiClient.retrofit.create(ContactApi::class.java)

    val colors = ArrayList<String>()
    private var progressBar: ProgressDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).changeStatusBarColor(Color.parseColor("#0E5F9E"))
     //   (activity as AppCompatActivity).changeNavigationBarColor(Color.parseColor("#0E5F9E"))
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        addContact.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.layoutFragment,
                AddCardFragment()
            )?.addToBackStack("add")?.commit()
        }

        loadColors()
        adapter = CardAdapter(colors)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(requireContext())


           init()

              moveVert.setOnClickListener { it ->
            val popup = PopupMenu(requireContext(),it)
            popup.inflate(R.menu.menu_main)
            popup.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.signOut ->{
                        val pref = SharedPreferences(requireContext())
                        pref.setString("SWITCH","")
                        fragmentManager?.beginTransaction()?.replace(R.id.layoutFragment,LoginFragment())
                            ?.commit()
                    }

                    else ->{}
                }
                true
            }
            popup.show()

        }
        swipeRefresh.setOnRefreshListener {
            run {
                init()
                swipeRefresh.isRefreshing = false
            }
             }
    }

    private fun loadColors() {
        colors.add("#3F51B5")
        colors.add("#4CAF50")
        colors.add("#FFC107")
        colors.add("#009688")
        colors.add("#8BC34A")
        colors.add("#9C27B0")
    }

    private fun init() {
        showProgressBar()
        executor.execute {
            try {
                val res = api.getAll().execute().body()
                when{
                    res == null ->{
                        activity?.runOnUiThread {
                        progressHide()
                   //     Toast.makeText(requireContext(), R.string.empty_body, Toast.LENGTH_SHORT).show()
                    }}
                    res.status == "ERROR"->{
                        activity?.runOnUiThread {
                            progressHide()
                            Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    res.status == "OK"->{
                        if (res.data != null) {
                            progressHide()
                            room.deleteAll()
                            room.insertAll(res.data)
                            activity?.runOnUiThread {
                                adapter.submitList(res.data)
                            }

                        }
                    }
                }
            }catch (e:Throwable){
                activity?.runOnUiThread {
                    progressHide()
                    val text = when (e) {
                        is HttpException -> "Internetga ulanishda xatolik!"
                        else -> "Aniqlanmagan xatolik. Iltimos qayta urinib ko'ring."
                    }
                    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
                    executor.execute {
                       val ls = room.getAllFromRoom()
                        activity?.runOnUiThread { adapter.submitList(ls) }
                    }
                }
            }
        }
    }
    private fun showProgressBar(){
        progressBar = ProgressDialog(requireContext())
        progressBar!!.setMessage("please wait.....")
        progressBar!!.show()
    }

    private fun progressHide(){
        progressBar!!.dismiss()
    }

    /*  override fun showMessage(message: MessageData) {
          var text: String =  ""
          message.onMessage {
              text = it
          }.onResource {
              text = getString(it)
          }.onFailure {
              text = when (it) {
                  is HttpException -> "Internetga ulanishda xatolik!"
                  else -> "Aniqlanmagan xatolik. Iltimos qayta urinib ko'ring."
              }
          }
          Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
      }

      override fun loadList(ls: List<ContactData>) {
          adapter.submitList(ls)
      }

      override fun notifyRemove(contactData: ContactData) {
          val ls = adapter.currentList.toMutableList()
          ls.remove(contactData)
          adapter.submitList(ls)
      }

      override fun notifyAdd(contactData: ContactData) {
          val ls = adapter.currentList.toMutableList()
          ls.add(contactData)
          adapter.submitList(ls)
          list.smoothScrollToPosition(ls.size - 1)
      }

      override fun notifyEdit(contactData: ContactData) {
          val ls = adapter.currentList.toMutableList()
          val index = ls.indexOf(contactData)
          ls[index] = contactData
          adapter.submitList(ls)
          adapter.notifyItemChanged(index)
          list.smoothScrollToPosition(index)
      }

      override fun openDialogAdd() {
          val dialog = Dialog(requireContext(), "Add")
          dialog.setOnClickListener{ presenter.addContact(it)}
          dialog.show()
      }

      override fun progressBarShow() {
          progressBar = ProgressDialog(requireContext())
          progressBar!!.setMessage("please wait.....")
          progressBar!!.show()
      }

      override fun progressBarDismiss() {
          progressBar!!.dismiss()
      }

      override fun openDialogEdit(contactData: ContactData) {
          val dialog = Dialog(requireContext(), "Edit")
          dialog.setContactDialog(contactData)
          dialog.setOnClickListener { presenter.updateContact(it) }
          dialog.show()
      }*/

    }
