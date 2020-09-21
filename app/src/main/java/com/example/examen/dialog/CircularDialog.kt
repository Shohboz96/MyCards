package com.example.examen.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.example.examen.R
import kotlinx.android.synthetic.main.dialog_circular.*
import kotlinx.android.synthetic.main.dialog_circular.view.*

class CircularDialog(context: Context,i:Int) : AlertDialog(context) {
    private var listener: ((Int) -> Unit)? = null
    private var listenerToHome: ((Int) -> Unit)? = null
    private val contentView =
        LayoutInflater.from(context).inflate(R.layout.dialog_circular, null, false)

    init {

        setView(contentView)
        if(i  ==  1){
            contentView.succerFailImage.setImageResource(R.drawable.ic_baseline_cancel_24)
            contentView.succesFailText.text = "Transaction failed"
            contentView.done.text = "Try Again"
           // contentView.exit_to_home.visibility = View.VISIBLE
        }
        contentView.exit_to_home.setOnClickListener{
            listenerToHome?.invoke(2)
            dismiss()
        }
        contentView.done.setOnClickListener {
            listener?.invoke(1)
            dismiss() }
    }
    fun setOnClickListener(block:(Int) -> Unit) {
        listener = block
    }
}