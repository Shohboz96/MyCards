package com.example.examen.mvp

import com.example.examen.data.CardData
import com.example.examen.utils.MessageData
import com.example.examen.utils.ResultData

interface ContactContract {
    interface Model{
        fun add(contactData: CardData, block: (ResultData<CardData>) -> Unit)
        fun remove(contactData: CardData, block: (ResultData<CardData>) -> Unit)
        fun edit(contactData: CardData, block: (ResultData<CardData>) -> Unit)
        fun getAll(block: (ResultData<List<CardData>>) -> Unit)
        fun getAllFromRoom():List<CardData>
        fun insertAll(list:List<CardData>)
        fun deleteAll()
        fun deleteFromRoom(data: CardData)
        fun insertToRoom(data: CardData)
        fun updateInRoom(data: CardData)
    }
    interface View{
        fun showMessage(messageData: MessageData)
        fun loadList(ls: List<CardData>)
        fun notifyRemove(contactData: CardData)
        fun notifyAdd(contactData: CardData)
        fun notifyEdit(contactData: CardData)
        fun openDialogAdd()
        fun progressBarShow()
        fun progressBarDismiss()
        fun openDialogEdit(contactData: CardData)


    }
    interface Presenter{
        fun init()
        fun addContact(contactData: CardData)
        fun removeContact(contactData: CardData)
        fun openAddDialog()
        fun openEditDialog(contactData: CardData)
        fun updateContact(contactData: CardData)
    }


}