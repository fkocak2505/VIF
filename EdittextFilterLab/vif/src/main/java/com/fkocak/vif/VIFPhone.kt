package com.fkocak.vif

import android.content.Context
import android.text.InputFilter
import android.text.Spanned
import android.widget.EditText
import android.widget.Toast

class VIFPhone(var et: EditText, override var context: Context) : InputFilter, BaseVIF(context) {

    private var source: CharSequence? = null
    private var start: Int? = null
    private var end: Int? = null
    private var dest: Spanned? = null
    private var dstart: Int? = null
    private var dend: Int? = null

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {

        this.source = source
        this.start = start
        this.end = end
        this.dest = dest
        this.dstart = dstart
        this.dend = dend

        return null
    }

    //==============================================================================================
    // ACCESSING ACTIVITY OR FRAGMENT
    /**
     * et Functions..
     */
    fun etLoseFunction(ignoreFirstCharForPhoneNumber: String, areaCode: String): VIFPhone {
        et.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val phoneNumber = et.text.toString()

                checkFirstNumber(phoneNumber, ignoreFirstCharForPhoneNumber, areaCode)?.let {
                    if (it) {

                        showMessage(
                            "Telefon numarası $ignoreFirstCharForPhoneNumber ile başlamalıdır",
                            Toast.LENGTH_SHORT
                        )

                        et.text.clear()

                    }

                }

            }
        }

        return this
    }

    /**
     * check are code first digit is 5..
     */
    private fun checkFirstNumber(
        str: String,
        ignoreFirstCharForPhoneNumber: String,
        areaCode: String
    ): Boolean? {
        return if (str.isNotEmpty()) {
            if (str.length != 1) {
                when(str.length){
                    10 -> {
                        str[0].toString() != ignoreFirstCharForPhoneNumber
                    }
                    11 -> {
                        str[1].toString() != ignoreFirstCharForPhoneNumber
                    }
                    12 -> {
                        str[2].toString() != ignoreFirstCharForPhoneNumber
                    }
                    13->{
                        str[areaCode.length].toString() != ignoreFirstCharForPhoneNumber
                    }
                    else -> null
                }
            } else
                null
        } else
            null

    }
}