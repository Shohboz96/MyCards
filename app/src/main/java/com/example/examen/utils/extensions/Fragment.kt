package com.example.examen.utils.extensions

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.examen.R

fun Fragment.navigate(
    fragment: Fragment,addToBackStack: Boolean = false,popBackStack: Boolean = false,popUpTo: String = "",
    popUpToInclusive: Boolean = false, @IdRes resId: Int = R.id.layoutFragment
) {

    fragmentManager?.apply {
        val transaction = beginTransaction().replace(resId, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(fragment.javaClass.simpleName)
        }
        if (popBackStack) {
            popBackStack(popUpTo, if (popUpToInclusive) FragmentManager.POP_BACK_STACK_INCLUSIVE else 0)
        }
        transaction.commit()
    }
}