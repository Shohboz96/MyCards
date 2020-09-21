package com.example.examen.screens

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.examen.R
import kotlinx.android.synthetic.main.fragment_forget_code.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.examen.api.ApiClient
import com.example.examen.api.ContactApi
import com.example.examen.api.ResponseData
import com.example.examen.data.PasswordData
import com.example.examen.dialog.CircularDialog
import com.example.examen.utils.extensions.changeNavigationBarColor
import com.example.examen.utils.extensions.changeStatusBarColor
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.fragment_verify_card.*
import java.util.concurrent.Executors

class ForgetCodeFragment : Fragment(R.layout.fragment_forget_code) {

    private val api = ApiClient.retrofit.create(ContactApi::class.java)
    private val executor = Executors.newSingleThreadExecutor()
    var number = ""
    var pass = ""
    private var progressBar: ProgressDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).changeStatusBarColor(Color.GRAY)
        (activity as AppCompatActivity).changeNavigationBarColor(Color.GRAY)


        back_arrow_forgotCode.setOnClickListener {
            fragmentManager?.popBackStack("number_to_code", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            fragmentManager?.beginTransaction()?.replace(R.id.layoutFragment,
                ForgetNumberFragment()
            )
                ?.commit()

        }

        val bundle = this.arguments
        if(bundle != null){
            number = bundle.getString("NUMBER","").toString()
            pass = bundle.getString("NEW_PASSWORD","").toString()
        }
        maskedText()
        button_forgetCode.setOnClickListener {
            val code = code_forgetCode.text.toString()

            when {
                code.isEmpty() -> {
                    code_forgetCode.error = getString(R.string.enterCode)
                    return@setOnClickListener
                }
                code.length < 6 -> {
                    code_forgetCode.error = getString(R.string.wrongCode)
                    return@setOnClickListener
                }

                else -> {
                    showProgressBar()
                    val data = PasswordData(number, pass, code)
                    api.password(data).enqueue(object : Callback<ResponseData<Any>> {
                        override fun onFailure(call: Call<ResponseData<Any>>, t: Throwable) {
                            activity?.runOnUiThread {
                                progressHide()

                                val dialog = CircularDialog(requireContext(),1)
                                dialog.setOnClickListener {
                                    if(it == 2){
                                        fragmentManager?.popBackStack(
                                            "login_to_forget",
                                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                                        )
                                        fragmentManager?.popBackStack(
                                            "number_to_code",
                                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                                        )
                                        fragmentManager?.popBackStack(
                                            "login_to_new",
                                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                                        )
                                        fragmentManager?.beginTransaction()
                                            ?.replace(R.id.layoutFragment, LoginFragment())
                                            ?.commit()
                                        fragmentManager?.popBackStack()
                                    }
                                }
                                dialog.show()
                            }
                        }

                        override fun onResponse(
                            call: Call<ResponseData<Any>>,
                            response: Response<ResponseData<Any>>
                        ) {
                            val res = response.body()
                            when {
                                res == null -> {
                                    progressHide()

                                    Toast.makeText(requireContext(), "Null", Toast.LENGTH_SHORT).show()
                                }
                                res.status == "ERROR" -> {
                                    activity?.runOnUiThread {
                                        progressHide()

                                        val dialog = CircularDialog(requireContext(),1)
                                        dialog.setOnClickListener {
                                            if (it == 2) {
                                                fragmentManager?.popBackStack(
                                                    "login_to_forget",
                                                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                                                )
                                                fragmentManager?.popBackStack(
                                                    "number_to_code",
                                                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                                                )
                                                fragmentManager?.popBackStack(
                                                    "login_to_new",
                                                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                                                )
                                                fragmentManager?.beginTransaction()
                                                    ?.replace(R.id.layoutFragment, LoginFragment())
                                                    ?.commit()
                                                fragmentManager?.popBackStack()
                                            }
                                        }
                                        dialog.show()
                                    }
                                }
                                res.status == "OK" -> {
                                    activity?.runOnUiThread {
                                        progressHide()

                                        val dialog = CircularDialog(requireContext(),2)
                                        dialog.setOnClickListener {

                                            fragmentManager?.popBackStack(
                                                "login_to_forget",
                                                FragmentManager.POP_BACK_STACK_INCLUSIVE
                                            )
                                            fragmentManager?.popBackStack(
                                                "number_to_code",
                                                FragmentManager.POP_BACK_STACK_INCLUSIVE
                                            )
                                            fragmentManager?.popBackStack(
                                                "login_to_new",
                                                FragmentManager.POP_BACK_STACK_INCLUSIVE
                                            )
                                            fragmentManager?.beginTransaction()
                                                ?.replace(R.id.layoutFragment, LoginFragment())
                                                ?.commit()
                                            fragmentManager?.popBackStack()
                                        }
                                        dialog.show()
                                    }

                                    Log.d("AAA", "Ok")
                                }

                            }
                        }

                    })
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

    private fun maskedText() {
        val listener =
            MaskedTextChangedListener("[000][000]", true, code_forgetCode, null,
                object : MaskedTextChangedListener.ValueListener {
                    override fun onTextChanged(maskFilled: Boolean, extractedValue: String) {

                    }
                })
        code_forgetCode.apply {
            addTextChangedListener(listener)
            onFocusChangeListener = listener
        }
    }
}