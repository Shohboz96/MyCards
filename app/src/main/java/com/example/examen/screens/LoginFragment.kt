package com.example.examen.screens

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import com.example.examen.R
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.HttpException
import com.example.examen.api.ApiClient
import com.example.examen.api.ContactApi
import com.example.examen.data.LoginData
import com.example.examen.helper.SharedPreferences
import com.example.examen.utils.extensions.changeNavigationBarColor
import com.example.examen.utils.extensions.changeStatusBarColor
import java.util.concurrent.Executors

class LoginFragment : Fragment(R.layout.fragment_login){


    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var pref: SharedPreferences
    private var swit: Boolean = false
    private var progressBar: ProgressDialog? = null
    private val api = ApiClient.retrofit.create(ContactApi::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).changeStatusBarColor(Color.GRAY)
        (activity as AppCompatActivity).changeNavigationBarColor(Color.GRAY)

        pref = SharedPreferences(requireContext())
        pref.setString("INTRO","intro")

        switchBtn.setOnClickListener {
            swit = if (swit) {
                switchBtn.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24)
                false
            } else {
                switchBtn.setImageResource(R.drawable.ic_baseline_check_box_24)
                true
            }
        }
        forget_Password.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.layoutFragment, NewPasswordFragment())
                ?.addToBackStack("login_to_new")?.commit()
        }

        button_registration.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.layoutFragment, RegisterFragment())
                ?.addToBackStack("login")?.commit()
        }
        login_PhoneNumber.setOnEditorActionListener { textView, i, keyEvent ->
            if(i == EditorInfo.IME_ACTION_DONE){
                return@setOnEditorActionListener true
            }
            false
        }

        button_login.setOnClickListener {

            val loginPhoneNumber = "+998"+login_PhoneNumber.unmaskedText.toString().trim()
            val loginPassword = login_Password.text.toString()
            when {
                loginPhoneNumber.isEmpty() -> {
                    login_PhoneNumber.error = getString(R.string.enterPhoneNumber)
                    return@setOnClickListener
                }
                loginPhoneNumber.length != 13 -> {
                    login_PhoneNumber.error = getString(R.string.wrongPhoneNumber)
                    return@setOnClickListener
                }
                loginPassword.isEmpty() -> {
                    login_Password.setError(getString(R.string.enterPassword),R.drawable.ic_baseline_remove_red_eye_24.toDrawable())
                    return@setOnClickListener
                }
                loginPassword.length < 6 -> {
                    login_Password.setError(getString(R.string.wrongPassword),R.drawable.ic_baseline_remove_red_eye_24.toDrawable())
                    return@setOnClickListener
                }
                else -> {
                    showProgressBar()
                    executor.execute {
                        try {
                            val log = LoginData(loginPhoneNumber, loginPassword)
                            val res = api.login(log).execute().body()
                            when{
                                res == null ->{
                                    activity?.runOnUiThread {
                                        progressHide()
                                        Toast.makeText(
                                            requireContext(),
                                            R.string.empty_body,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                res.status == "ERROR"->{
                                    activity?.runOnUiThread {
                                        progressHide()
                                        Toast.makeText(
                                            requireContext(),
                                            res.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                res.status == "OK" ->{
                                    activity?.runOnUiThread {
                                        progressHide()
                                        if (res.data != null) {
                                            pref.setUserToken(res.data)
                                            fragmentManager?.beginTransaction()
                                                ?.replace(R.id.layoutFragment, MainFragment())
                                                ?.commit()
                                            fragmentManager?.popBackStack()
                                            if (swit) {
                                                pref.setString("SWITCH", "switch")
                                            }

                                        } else {
                                            Toast.makeText(
                                                requireContext(),
                                                "Xatolik",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            }
                        }catch (e:Throwable){
                            activity?.runOnUiThread{
                                progressHide()
                                val text = when (e) {
                                    is HttpException -> "Internetga ulanishda xatolik!"
                                    else -> "Aniqlanmagan xatolik. Iltimos qayta urinib ko'ring."
                                }
                                Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
                            }
                        }

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

}

