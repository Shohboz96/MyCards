package com.example.examen.screensCard

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.examen.R
import com.example.examen.api.ApiClient
import com.example.examen.api.ContactApi
import com.example.examen.data.VerifyCardData
import com.example.examen.screens.MainFragment
import com.example.examen.utils.extensions.changeNavigationBarColor
import com.example.examen.utils.extensions.changeStatusBarColor
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.fragment_verify.*
import kotlinx.android.synthetic.main.fragment_verify_card.*
import retrofit2.HttpException
import java.util.concurrent.Executors

class VerifyCardFragment : Fragment(R.layout.fragment_verify_card) {

    var pan =""
    private val api = ApiClient.retrofit.create(ContactApi::class.java)
    private val executor = Executors.newSingleThreadExecutor()
    private var progressBar: ProgressDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (activity as AppCompatActivity).changeStatusBarColor(Color.GRAY)
        (activity as AppCompatActivity).changeNavigationBarColor(Color.GRAY)


        val bundle = this.arguments
        if(bundle != null){
            pan = bundle.getString("PAN","").toString()
        }
        maskedText()
        back_arrow_card_verify.setOnClickListener {
            fragmentManager?.popBackStack("add", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            fragmentManager?.beginTransaction()?.replace(R.id.layoutFragment, AddCardFragment())?.commit()
        }
        button_verify_card.setOnClickListener {
            val verifyCode = verify_card_code.text.toString()
            when {
                verifyCode.isEmpty() -> {
                    verify_code.error = "Enter Code"
                    return@setOnClickListener
                }
                verifyCode.length != 6 -> {
                    verify_code.error = "Short Code"
                    return@setOnClickListener
                }
                else -> {

                    showProgressBar()
                    executor.execute {
                        try {
                            val verify = VerifyCardData(pan, verifyCode)
                            val response = api.verifyCard(verify).execute().body()
                            when {
                                response == null -> {
                                    activity?.runOnUiThread {
                                        progressHide()
                                        Toast.makeText(requireContext(), R.string.empty_body, Toast.LENGTH_SHORT).show()
                                    }
                                }
                                response.status != "OK" -> {
                                    activity?.runOnUiThread {
                                        progressHide()
                                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                                response.status == "OK" -> {
                                    activity?.runOnUiThread {
                                        progressHide()
                                        if (response.data != null) {
                                            fragmentManager?.popBackStack("add", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                                            fragmentManager?.popBackStack("code", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                                            fragmentManager?.beginTransaction()?.replace(R.id.layoutFragment, MainFragment())?.commit()

                                            Toast.makeText(requireContext(), "Successfully", Toast.LENGTH_SHORT).show()
                                        }
                                        //fragmentManager?.popBackStack()
                                    }
                                }
                            }
                        } catch (e: Throwable) {
                            activity?.runOnUiThread {
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
    private fun maskedText() {
        val listener =
            MaskedTextChangedListener("[000][000]", true, verify_card_code, null,
                object : MaskedTextChangedListener.ValueListener {
                    override fun onTextChanged(maskFilled: Boolean, extractedValue: String) {

                    }
                })
        verify_card_code.apply {
            addTextChangedListener(listener)
            onFocusChangeListener = listener
        }
    }

}