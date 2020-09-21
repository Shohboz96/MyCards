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
import com.example.examen.data.AddCardData
import com.example.examen.data.ColorsData
import com.example.examen.screens.MainFragment
import com.example.examen.utils.extensions.changeNavigationBarColor
import com.example.examen.utils.extensions.changeStatusBarColor
import kotlinx.android.synthetic.main.fragment_add_card.*
import retrofit2.HttpException
import java.util.concurrent.Executors

class AddCardFragment : Fragment(R.layout.fragment_add_card) {

    private val api = ApiClient.retrofit.create(ContactApi::class.java)
    val data = ArrayList<ColorsData>()
    private val executor = Executors.newSingleThreadExecutor()
    private var countColor = -1
    private var progressBar: ProgressDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (activity as AppCompatActivity).changeStatusBarColor(Color.GRAY)
        (activity as AppCompatActivity).changeNavigationBarColor(Color.GRAY)

        add_card_arrow.setOnClickListener {
            fragmentManager?.popBackStack("add", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            fragmentManager?.beginTransaction()?.replace(R.id.layoutFragment, MainFragment())?.commit()

        }

        card1.scaleX = 0.8f
        card1.scaleY = 0.8f
        countColor = 1

        card1.setOnClickListener {
            countColor = 1
            card0.setCardBackgroundColor(Color.parseColor("#3F51B5"))
            card1.scaleX = 0.8f
            card1.scaleY = 0.8f
            card2.scaleY = 1.0f
            card3.scaleY = 1.0f
            card4.scaleY = 1.0f
            card5.scaleY = 1.0f
            card6.scaleY = 1.0f
            card2.scaleX = 1.0f
            card3.scaleX = 1.0f
            card4.scaleX = 1.0f
            card5.scaleX = 1.0f
            card6.scaleX = 1.0f
        }
        card2.setOnClickListener {
            countColor = 2
            card0.setCardBackgroundColor(Color.parseColor("#4CAF50"))
            card1.scaleX = 1.0f
            card2.scaleX = 0.8f
            card1.scaleY = 1.0f
            card3.scaleX = 1.0f
            card4.scaleX = 1.0f
            card5.scaleX = 1.0f
            card6.scaleX = 1.0f
            card2.scaleY = 0.8f
            card3.scaleY = 1.0f
            card4.scaleY = 1.0f
            card5.scaleY = 1.0f
            card6.scaleY = 1.0f
        }
        card3.setOnClickListener {
            countColor = 3
            card0.setCardBackgroundColor(Color.parseColor("#FFC107"))
            card1.scaleX = 1.0f
            card2.scaleX = 1.0f
            card3.scaleX = 0.8f
            card1.scaleY = 1.0f
            card4.scaleX = 1.0f
            card5.scaleX = 1.0f
            card6.scaleX = 1.0f
            card2.scaleY = 1.0f
            card3.scaleY = 0.8f
            card4.scaleY = 1.0f
            card5.scaleY = 1.0f
            card6.scaleY = 1.0f
        }
        card4.setOnClickListener {
            countColor = 4
            card0.setCardBackgroundColor(Color.parseColor("#009688"))
            card1.scaleX = 1.0f
            card2.scaleX = 1.0f
            card3.scaleX = 1.0f
            card4.scaleX = 0.8f
            card1.scaleY = 1.0f
            card5.scaleX = 1.0f
            card6.scaleX = 1.0f
            card2.scaleY = 1.0f
            card3.scaleY = 1.0f
            card4.scaleY = 0.8f
            card5.scaleY = 1.0f
            card6.scaleY = 1.0f
        }
        card5.setOnClickListener {
            countColor = 5
            card0.setCardBackgroundColor(Color.parseColor("#8BC34A"))
            card1.scaleX = 1.0f
            card2.scaleX = 1.0f
            card3.scaleX = 1.0f
            card4.scaleX = 1.0f
            card5.scaleX = 0.8f
            card1.scaleY = 1.0f
            card6.scaleX = 1.0f
            card2.scaleY = 1.0f
            card3.scaleY = 1.0f
            card4.scaleY = 1.0f
            card5.scaleY = 0.8f
            card6.scaleY = 1.0f
        }
        card6.setOnClickListener {
            countColor = 6
                card0.setCardBackgroundColor(Color.parseColor("#9C27B0"))
            card1.scaleX = 1.0f
            card2.scaleX = 1.0f
            card3.scaleX = 1.0f
            card4.scaleX = 1.0f
            card5.scaleX = 1.0f
            card6.scaleX = 0.8f
            card1.scaleY = 1.0f
            card2.scaleY = 1.0f
            card3.scaleY = 1.0f
            card4.scaleY = 1.0f
            card5.scaleY = 1.0f
            card6.scaleY = 0.8f
        }

        btn_add_card.setOnClickListener {
            val pan = karta_nomeri.rawText.toString().trim()
            val expiredAt = muddati.text.toString().trim()
            val name = karta_nomi.text.toString().trim()

            when {
                pan.isEmpty() -> {
                    karta_nomeri.error = getString(R.string.karta_nomeri)
                    return@setOnClickListener
                }
                pan.length != 16 -> {
                    karta_nomeri.error = getString(R.string.short_number)
                    return@setOnClickListener
                }
                expiredAt.isEmpty() -> {
                    muddati.error = getString(R.string.enterDate)

                    return@setOnClickListener
                }
                expiredAt.length != 5 -> {
                      muddati.error = getString(R.string.shortDate)
                    return@setOnClickListener
                }
                name.isEmpty() -> {
                    karta_nomi.error = getString(R.string.karta_nomi)
                    return@setOnClickListener
                }
                name.length <3 -> {
                    karta_nomi.error = getString(R.string.shortCardName)
                    return@setOnClickListener
                }
                countColor == -1 ->{
                    Toast.makeText(requireContext(),"Please choose color",Toast.LENGTH_SHORT).show()
                }
                else -> {
                    showProgressBar()
                    executor.execute {
                        try {
                            val data = AddCardData(pan,expiredAt,name,countColor)
                            val  res = api.addCard(data).execute().body()
                            when{
                                res == null ->{}
                                res.status != "OK" ->{
                                    activity?.runOnUiThread {
                                        progressHide()
                                        Toast.makeText(requireContext(),res.message,Toast.LENGTH_SHORT).show()
                                    }
                                }
                                res.status == "OK" ->{
                                    activity?.runOnUiThread {
                                        progressHide()
                                        val bundle = Bundle()
                                        bundle.putString("PAN",pan)
                                        fragmentManager?.beginTransaction()?.replace(R.id.layoutFragment,
                                            VerifyCardFragment()
                                                .apply { arguments = bundle }
                                        )?.addToBackStack("code")
                                            ?.commit()
                                    }
                                }
                            }

                        }catch (e:Throwable){
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

}