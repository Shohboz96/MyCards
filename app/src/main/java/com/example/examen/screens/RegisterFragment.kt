package com.example.examen.screens

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.examen.R
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.fragment_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import com.example.examen.api.ApiClient
import com.example.examen.api.ContactApi
import com.example.examen.api.ResponseData
import com.example.examen.data.ContactUserData
import com.example.examen.helper.SharedPreferences
import com.example.examen.utils.extensions.changeNavigationBarColor
import com.example.examen.utils.extensions.changeStatusBarColor
import java.util.concurrent.Executors

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val api = ApiClient.retrofit.create(ContactApi::class.java)
    private val executor = Executors.newSingleThreadExecutor()
    private var progressBar: ProgressDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).changeStatusBarColor(Color.GRAY)
        (activity as AppCompatActivity).changeNavigationBarColor(Color.GRAY)

        val pref = SharedPreferences(requireContext())
        pref.setString("INTRO","intro")

        back_arrow_register.setOnClickListener {
            fragmentManager?.popBackStack("login", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            fragmentManager?.beginTransaction()?.replace(R.id.layoutFragment, LoginFragment())
                ?.commit()

        }

        button_register.setOnClickListener {
            Log.d("AAA", "registerOpen")
            val phoneNumber = "+998"+inputPhoneNumber.unmaskedText.toString().trim()
            val password = inputPassword.text.toString()
            val confirmPassword = inputConfirmPassword.text.toString()
            val lastName = inputLastName.text.toString()
            val firstName = inputFirstName.text.toString()

            when {
                firstName.isEmpty() -> {
                    inputFirstName.error = getString(R.string.enterFirstName)
                    return@setOnClickListener
                }
                firstName.length < 3 -> {
                    inputFirstName.error = getString(R.string.wrongFirstName)
                    return@setOnClickListener
                }
                lastName.isEmpty() -> {
                    inputLastName.error = getString(R.string.enterLastName)
                    return@setOnClickListener
                }
                lastName.length < 3 -> {
                    inputLastName.error = getString(R.string.wrongLastName)
                }
                phoneNumber.isEmpty() -> {
                    inputPhoneNumber.error = getString(R.string.enterPhoneNumber)
                    return@setOnClickListener
                }
                phoneNumber.length != 13 -> {
                    inputPhoneNumber.error = getString(R.string.wrongPhoneNumber)
                }

                password.isEmpty() -> {
                    inputPassword.setError(getString(R.string.enterPassword),R.drawable.ic_baseline_remove_red_eye_24.toDrawable())
                    return@setOnClickListener
                }
                password.length < 6 -> {
                    inputPassword.setError(getString(R.string.wrongPassword),R.drawable.ic_baseline_remove_red_eye_24.toDrawable())
                    return@setOnClickListener
                }
                confirmPassword.isEmpty() -> {
                    inputConfirmPassword.setError(getString(R.string.enterConPassword),R.drawable.ic_baseline_remove_red_eye_24.toDrawable())
                    return@setOnClickListener
                }
                password != confirmPassword->{
                    inputConfirmPassword.setError(getString(R.string.wrongConPassword),R.drawable.ic_baseline_remove_red_eye_24.toDrawable())
                    return@setOnClickListener
                }

                else -> {
                    showProgressBar()
                    val user = ContactUserData(phoneNumber, password, lastName, firstName)

                    api.addUser(user).enqueue(object : Callback<ResponseData<ContactUserData>> {
                        override fun onFailure(
                            call: Call<ResponseData<ContactUserData>>,
                            t: Throwable
                        ) {
                            activity?.runOnUiThread{
                                progressHide()
                                val text = when (t) {
                                    is HttpException -> "Internetga ulanishda xatolik!"
                                    else -> "Aniqlanmagan xatolik. Iltimos qayta urinib ko'ring."
                                }
                                Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onResponse(
                            call: Call<ResponseData<ContactUserData>>,
                            response: Response<ResponseData<ContactUserData>>
                        ) {
                            val res = response.body()
                            when {
                                res == null -> {
                                    activity?.runOnUiThread {
                                        progressHide()
                                        Toast.makeText(
                                            requireContext(),
                                            R.string.empty_body,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                res.status == "ERROR" -> {
                                    activity?.runOnUiThread {
                                        progressHide()
                                        Toast.makeText(
                                            requireContext(),
                                            res.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                }
                                res.status == "OK" -> {
                                    activity?.runOnUiThread {
                                        progressHide()
                                        val bundle = Bundle()
                                        bundle.putString("number",phoneNumber)
                                        fragmentManager?.beginTransaction()?.replace(R.id.layoutFragment,
                                            VerifyFragment()
                                                .apply { arguments = bundle })
                                            ?.addToBackStack("register")?.commit()
                                    }
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
}