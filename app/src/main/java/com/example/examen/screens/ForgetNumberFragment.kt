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
import kotlinx.android.synthetic.main.fragment_forget_number.*
import retrofit2.HttpException
import com.example.examen.api.ApiClient
import com.example.examen.api.ContactApi
import com.example.examen.data.ResetData
import com.example.examen.utils.extensions.changeNavigationBarColor
import com.example.examen.utils.extensions.changeStatusBarColor
import java.util.concurrent.Executors

class ForgetNumberFragment : Fragment(R.layout.fragment_forget_number) {
    private val api = ApiClient.retrofit.create(ContactApi::class.java)
    private val executor = Executors.newSingleThreadExecutor()
    private var progressBar: ProgressDialog? = null

    var newPas = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (activity as AppCompatActivity).changeStatusBarColor(Color.GRAY)
        (activity as AppCompatActivity).changeNavigationBarColor(Color.GRAY)

        val bundle = this.arguments
        if(bundle != null){
            newPas = bundle.getString("NEW","").toString()
        }

        back_arrow_forgotNumber.setOnClickListener {
            fragmentManager?.popBackStack("new_to_number",FragmentManager.POP_BACK_STACK_INCLUSIVE)
            fragmentManager?.beginTransaction()?.replace(R.id.layoutFragment, LoginFragment())
               ?.commit()

        }

        button_forgot_number.setOnClickListener {
            val number = "+998"+forgot_number.unmaskedText.toString()
            if (number.isEmpty()) {
                forgot_number.error = "Enter Phone Number"
                return@setOnClickListener
            } else if (number.length < 13) {
                forgot_number.error = "Short Phone Number"
                return@setOnClickListener
            } else {
                showProgressBar()
                val resEt = ResetData(number)
                executor.execute {
                    try {
                        val res = api.reset(resEt).execute().body()
                        when {
                            res == null -> {
                                activity?.runOnUiThread {
                                    progressHide()
                                    Toast.makeText(requireContext(), R.string.empty_body, Toast.LENGTH_SHORT).show()
                                }

                            }
                            res.status == "ERROR" -> {
                                activity?.runOnUiThread {
                                    progressHide()
                                    Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT).show()
                                }

                            }
                            res.status == "OK" -> {
                                progressHide()
                                val bundle = Bundle()
                                bundle.putString("NUMBER",number)
                                bundle.putString("NEW_PASSWORD",newPas)
                                fragmentManager?.beginTransaction()?.replace(R.id.layoutFragment,
                                    ForgetCodeFragment().apply { arguments = bundle })
                                    ?.addToBackStack("number_to_code")
                                    ?.commit()

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

    private fun showProgressBar(){
        progressBar = ProgressDialog(requireContext())
        progressBar!!.setMessage("please wait.....")
        progressBar!!.show()
    }

    private fun progressHide(){
        progressBar!!.dismiss()
    }
}