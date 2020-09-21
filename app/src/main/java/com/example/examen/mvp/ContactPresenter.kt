package com.example.examen.mvp

import android.util.Log
import com.example.examen.data.CardData

class ContactPresenter(
    private val view: ContactContract.View,
    private val  repository: ContactContract.Model
) : ContactContract.Presenter{

   override fun init() {
       //show
       view.progressBarShow()
       repository.getAll{
           //hide
           view.progressBarDismiss()
          it.onData {
              Log.d("AAA","onData")
              repository.insertAll(it)
              view.loadList(it)
          }.onMessageData {
              view.loadList(repository.getAllFromRoom())
              Log.d("AAA","onMessageData")
              view.showMessage(it)
          }.onFailure {
              Log.d("AAA","onFailure")
              view.loadList(repository.getAllFromRoom())

          }
       }
   }

    override fun addContact(contactData: CardData) {
        view.progressBarShow()
        repository.add(contactData){
            view.progressBarDismiss()
            it.onData{
               repository.insertToRoom(it)
                view.notifyAdd(it)}
                .onMessageData (view::showMessage)
        }
    }

    override fun removeContact(contactData: CardData) {
        view.progressBarShow()
        repository.remove(contactData) {
            view.progressBarDismiss()
            it.onData {
                repository.deleteFromRoom(it)
                view.notifyRemove(it)
            }.onMessageData{
                Log.d("AAA","onMessageData")
                view.progressBarDismiss()
                view.showMessage(it)
            }.onFailure {
                Log.d("AAA","onFailure")
                view.progressBarDismiss()
              //  view.showMessage(it)
            }

        }
    }

    override fun openAddDialog() {
        view.openDialogAdd()
    }

    override fun openEditDialog(contactData: CardData) {
        view.openDialogEdit(contactData)
    }

    override fun updateContact(contactData: CardData) {
        view.progressBarShow()
        repository.edit(contactData){ it ->
            view.progressBarDismiss()
            it.onData{
                repository.updateInRoom(it)
                view.notifyEdit(it)
            }.onMessageData (view::showMessage)
        }
    }


}