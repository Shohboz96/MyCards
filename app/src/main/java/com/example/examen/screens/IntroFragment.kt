package com.example.examen.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.examen.R
import com.example.examen.adapter.IntroAdapter
import com.example.examen.data.IntoData
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_intro.*

class IntroFragment : Fragment(R.layout.fragment_intro) {

    val data = ArrayList<IntoData>()
    val adapter = IntroAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        loadData()
        intro_pager.adapter = adapter
        adapter.submitList(data)

        intro_pager.registerOnPageChangeCallback(pageChangeCallback())
        TabLayoutMediator(intro_tabLayout,intro_pager){
                _, _ ->

        }.attach()

        intro_login.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.layoutFragment, LoginFragment())
                ?.commit()
        }
        intro_reg.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.layoutFragment, RegisterFragment())
                ?.commit()
        }


    }

    private fun loadData() {
        data.add(IntoData("The simple and secure way\n to  send, spend, and \n manage your money.",
            R.drawable.ic_credit_card))
        data.add(IntoData("Send money fast \n to anyone in 100+ countries.",
            R.drawable.ic_money))
        data.add(IntoData("Spend securely on \n millions of sites and apps.",
            R.drawable.ic_payment))
    }
    private fun pageChangeCallback() = object : ViewPager2.OnPageChangeCallback(){
        override fun onPageSelected(position: Int) {
         //   val d = data[position].contentColor
       //     (activity as AppCompatActivity).changeStatusBarColor(d)
           // (activity as AppCompatActivity).changeNavigationBarColor(d)
        }
    }

}