package com.example.examen.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.examen.R
import com.example.examen.data.IntoData
import com.example.examen.utils.extensions.bindItem
import com.example.examen.utils.extensions.inflate
import kotlinx.android.synthetic.main.fragment_intro.view.*
import kotlinx.android.synthetic.main.item_intro.view.*

class IntroAdapter :RecyclerView.Adapter<IntroAdapter.VHolder>(){

    val ls = ArrayList<IntoData>()
    fun submitList(list:List<IntoData>){
        ls.clear()
        ls.addAll(list)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VHolder(parent.inflate(
        R.layout.item_intro
    ))

    override fun getItemCount() = ls.size

    override fun onBindViewHolder(holder: VHolder, position: Int)  = holder.bind()

    inner class VHolder(view:View):RecyclerView.ViewHolder(view){
        fun bind() = bindItem {
            val d = ls[adapterPosition]
       //     intro_content.setBackgroundColor(d.contentColor)
            intro_textTop.text = d.textTop
            intro_image.setBackgroundResource(d.imageUrl)
          //  intro_textBottom.text = d.textBottom
        }
    }
}