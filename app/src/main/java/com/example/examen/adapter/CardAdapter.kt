package com.example.examen.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.examen.R
import com.example.examen.data.CardData
import com.example.examen.utils.SingleBlock
import com.example.examen.utils.extensions.bindItem
import com.example.examen.utils.extensions.inflate
import kotlinx.android.synthetic.main.item_card.view.*

class CardAdapter(val colors:List<String>) : ListAdapter<CardData, CardAdapter.ViewHolder>(
    CardData.ITEM_CALLBACK) {

    private var listenerEdit: SingleBlock<CardData>? = null
    private var listenerDelete: SingleBlock<CardData>? = null
    private var listener: SingleBlock<CardData>? = null

    fun setOnItemClickListener(block: SingleBlock<CardData>){
        listener = block
    }
    fun setOnItemEditClickListener(block: SingleBlock<CardData>){
        listenerEdit = block
    }
    fun setOnDeleteClickListener(block: SingleBlock<CardData>){
        listenerDelete = block
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(
        R.layout.item_card
    ))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener {
                listener?.invoke(getItem(adapterPosition))
            }

        }

        @SuppressLint("SetTextI18n")
        fun bind() = bindItem {
            itemView.apply {

                val d = getItem(adapterPosition)
                if(d.name?.length!! > 2){
                  name_card.text = d.name
                }else{
                    name_card.text = "Null"
                }
                card_text_number.text = d.pan?.substring(0, 4) + " **** **** " + d.pan?.substring(12, 16)
                card_muddati.text = d.expiredAt

                val a = String.format("%,d", d.amount).replace(',', ' ')
                balance.text = "$a so'm"
                item_card_content.setCardBackgroundColor(
                    when (d.color) {
                        1 -> Color.parseColor(colors[0])
                        2 -> Color.parseColor(colors[1])
                        3 -> Color.parseColor(colors[2])
                        4 -> Color.parseColor(colors[3])
                        5 -> Color.parseColor(colors[4])
                        6 -> Color.parseColor(colors[5])
                        else -> {
                            Color.parseColor(colors[0])
                        }

                    }
                )
            }
        }
    }
    private fun formatCard(cardNumber: String): String? {
        val delimiter = ' '
        return cardNumber.replace(".{4}(?!$)".toRegex(), "$0$delimiter")
    }

}
    //String.format("%,d", d.amount).replace(',', ' ')