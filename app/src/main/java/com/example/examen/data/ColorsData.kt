package com.example.examen.data

import androidx.recyclerview.widget.DiffUtil

data class ColorsData(
    val id:Int,
    val color:Int
){
    companion object{
        val ITEM_CALLBACK = object : DiffUtil.ItemCallback<ColorsData>() {
            override fun areItemsTheSame(oldItem: ColorsData, newItem: ColorsData) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ColorsData, newItem: ColorsData) = oldItem.color == newItem.color

        }
    }
}