package com.example.examen.screens

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.examen.R
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.fragment_verify.*
import retrofit2.HttpException
import com.example.examen.api.ApiClient
import com.example.examen.api.ContactApi
import com.example.examen.data.SmsCodeData
import com.example.examen.helper.SharedPreferences
import com.example.examen.utils.extensions.changeNavigationBarColor
import com.example.examen.utils.extensions.changeStatusBarColor
import java.util.concurrent.Executors


class VerifyFragment : Fragment(R.layout.fragment_verify) {

    private val api = ApiClient.retrofit.create(ContactApi::class.java)
    private val executor = Executors.newSingleThreadExecutor()
    var phoneNumber = ""
    private var progressBar: ProgressDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).changeStatusBarColor(Color.GRAY)
        (activity as AppCompatActivity).changeNavigationBarColor(Color.GRAY)


        val bundle = this.arguments
        if(bundle != null){
            phoneNumber = bundle.getString("number","").toString()
        }

        back_arrow_register_verify.setOnClickListener {
            fragmentManager?.popBackStack("register", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            fragmentManager?.beginTransaction()?.replace(R.id.layoutFragment,
                RegisterFragment()
            )
                ?.commit()

        }


        maskedText()

        button_verify.setOnClickListener {
            val verifyCode = verify_code.text.toString()
            when {
                verifyCode.isEmpty() -> {
                    verify_code.error = "Enter Code"
                    return@setOnClickListener
                }
                verifyCode.length != 6 -> {
                    verify_code.error = "Wrong Code"
                    return@setOnClickListener
                }
                phoneNumber != null -> {
                    showProgressBar()
                    val verify = SmsCodeData(phoneNumber, verifyCode)
                    val pref = SharedPreferences(requireContext())
                    executor.execute {
                        try {
                            val response = api.verify(verify).execute().body()
                            when {
                                response == null -> {
                                    progressHide()
                                    Toast.makeText(requireContext(), "null", Toast.LENGTH_SHORT).show()
                                }
                                response.status == "ERROR" -> {
                                    progressHide()
                                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT)
                                        .show()
                                }
                                response.status == "OK" -> {
                                    activity?.runOnUiThread {
                                        progressHide()
                                        if (response.data != null) {
                                            fragmentManager?.popBackStack(
                                                "register",
                                                FragmentManager.POP_BACK_STACK_INCLUSIVE
                                            )
                                            fragmentManager?.popBackStack(
                                                "login",
                                                FragmentManager.POP_BACK_STACK_INCLUSIVE
                                            )
                                            fragmentManager?.beginTransaction()
                                                ?.replace(R.id.layoutFragment,
                                                    MainFragment()
                                                )
                                                ?.commit()
                                            Toast.makeText(
                                                requireContext(),
                                                "Successfully registered",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            pref.setUserToken(response.data)
                                        }
                                        fragmentManager?.popBackStack()

                                    }
                                }
                            }
                        } catch (e: Throwable) {
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
    private fun maskedText() {
        val listener =
            MaskedTextChangedListener("[000][000]", true, verify_code, null,
                object : MaskedTextChangedListener.ValueListener {
                    override fun onTextChanged(maskFilled: Boolean, extractedValue: String) {

                    }
                })
        verify_code.apply {
            addTextChangedListener(listener)
            onFocusChangeListener = listener
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