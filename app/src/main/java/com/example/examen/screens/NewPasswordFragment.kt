package com.example.examen.screens

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.FragmentManager
import com.example.examen.R
import com.example.examen.utils.extensions.changeNavigationBarColor
import com.example.examen.utils.extensions.changeStatusBarColor
import kotlinx.android.synthetic.main.fragment_new_password.*


class NewPasswordFragment : Fragment(R.layout.fragment_new_password) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).changeStatusBarColor(Color.GRAY)
        (activity as AppCompatActivity).changeNavigationBarColor(Color.GRAY)

        back_arrow_newPassword.setOnClickListener {
            fragmentManager?.popBackStack("login_to_new", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            fragmentManager?.beginTransaction()?.replace(R.id.layoutFragment, LoginFragment())
                ?.commit()
        }

        button_newPassword.setOnClickListener {
            val pass = password_newPassword.text.toString()
            val conPass = comfirm_password_newPassword.text.toString()

            when{
                pass.isEmpty() -> {
                    password_newPassword.setError(getString(R.string.enterPassword),R.drawable.ic_baseline_remove_red_eye_24.toDrawable())
                    return@setOnClickListener
                }
                pass.length < 6 -> {
                    password_newPassword.setError(getString(R.string.wrongPassword),R.drawable.ic_baseline_remove_red_eye_24.toDrawable())
                    return@setOnClickListener
                }
                conPass.isEmpty() -> {
                    comfirm_password_newPassword.setError(getString(R.string.enterConPassword),R.drawable.ic_baseline_remove_red_eye_24.toDrawable())
                    return@setOnClickListener
                }
                pass != conPass->{
                    comfirm_password_newPassword.setError(getString(R.string.wrongConPassword),R.drawable.ic_baseline_remove_red_eye_24.toDrawable())
                    return@setOnClickListener
                }
                else ->{
                    val bundle = Bundle()
                    bundle.putString("NEW",pass)
                    fragmentManager?.beginTransaction()?.replace(R.id.layoutFragment,
                        ForgetNumberFragment().apply { arguments = bundle })
                        ?.addToBackStack("new_to_number")?.commit()
                }
            }
        }
    }



}