package com.example.examen.utils.extensions

import android.widget.PopupMenu
import java.lang.reflect.Field
import java.lang.reflect.Method

fun PopupMenu.setForceShowIcon(){
    try {
        val fields: Array<Field> = this.javaClass.declaredFields
        for (field in fields) {
            if ("mPopup" == field.name) {
                field.isAccessible = true
                val menuPopupHelper: Any = field.get(this)!!
                val classPopupHelper = Class.forName(
                    menuPopupHelper
                        .javaClass.name
                )
                val setForceIcons: Method = classPopupHelper.getMethod(
                    "setForceShowIcon", Boolean::class.javaPrimitiveType
                )
                setForceIcons.invoke(menuPopupHelper, true)
                break
            }
        }
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}
