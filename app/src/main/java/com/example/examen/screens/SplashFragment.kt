package com.example.examen.screens

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.examen.R
import com.example.examen.helper.SharedPreferences
import com.example.examen.screens.LoginFragment
import com.example.examen.screens.MainFragment
import com.example.examen.screensCard.AddCardFragment
import com.example.examen.screensCard.VerifyCardFragment
import com.example.examen.utils.extensions.changeNavigationBarColor
import com.example.examen.utils.extensions.changeStatusBarColor
import kotlinx.android.synthetic.main.fragment_splash.*
import java.util.concurrent.Executors

class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (activity as AppCompatActivity).changeStatusBarColor(Color.GRAY)
        (activity as AppCompatActivity).changeNavigationBarColor(Color.GRAY)

        Executors.newSingleThreadExecutor().execute {
            Thread.sleep(1200)

            val pref = SharedPreferences(requireContext())
            val transaction = fragmentManager?.beginTransaction()
            when {
                pref.getString("INTRO") != "intro" -> {
                    transaction?.replace(R.id.layoutFragment, IntroFragment()) //intro
                    transaction?.commit()
                }
                pref.getString("SWITCH") != "switch" -> {
                    transaction?.replace(R.id.layoutFragment, LoginFragment()) //login
                    transaction?.commit()
                }
                else -> {
                    transaction?.replace(R.id.layoutFragment, MainFragment())
                    //  transaction.addToBackStack("main")
                    transaction?.commit()
                }
            }
        }

    }
}