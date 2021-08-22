package com.fkocak.vif

import android.content.Context
import android.text.InputFilter
import android.text.Spanned
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import java.util.regex.Pattern

class VIFEmail(var et: EditText, override var context: Context) : InputFilter, BaseVIF(context) {

    private var source: String = ""
    private var start: Int? = null
    private var end: Int? = null
    private var dest: Spanned? = null
    private var dstart: Int? = null
    private var dend: Int? = null

    private var iMultipleSymbol: String = ""
    private var iFirstSymbol = mutableListOf<String>()
    private var iConsecutiveChars: String = ""
    private var iTwoConsecutiveChars = mutableListOf<String>()

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {

        this.source = source.toString()
        this.start = start
        this.end = end
        this.dest = dest
        this.dstart = dstart
        this.dend = dend

        for (i in start until end) {
            val regex = "[a-zA-Z0-9@._-]"

            val checkMe = source?.get(i).toString()
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(checkMe)

            when (matcher.matches()) {
                false -> {
                    showMessage(
                        "Email alanında ingilizce karakter ve \"@, ., _ , -\" özel karakterleri haricinde bir karakter kullanamazsınız.",
                        Toast.LENGTH_SHORT
                    )
                    return source?.dropLast(1)
                }
            }


            if (bIgnoreMultipleSpesificSymbol())
                return source?.dropLast(1)

            if (bIgnoreSpesificChar(checkMe))
                return source?.dropLast(1)

            if (bIgnoreConsecutiveChars())
                return source?.dropLast(1)

            if (bIgnoreTwoCharsConsecutive())
                return source?.dropLast(1)
        }

        return this.source
    }

    //==============================================================================================
    // ACCESSING ACTIVITY OR FRAGMENT
    /**
     * et loses focus..
     */
    fun etLosesFocus(ignoreLastSpesificChar: String, cannotMoreThanCharecter: Int): VIFEmail {
        et.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val finalTexEmail = et.text.toString()
                ignoreLastSpesificChar(finalTexEmail, ignoreLastSpesificChar)?.let {
                    if (it) {
                        showMessage(
                            "Son karaketer $ignoreLastSpesificChar olamaz",
                            Toast.LENGTH_SHORT
                        )
                        return@setOnFocusChangeListener
                    }
                }

                if (!isValidEmail(finalTexEmail)) {
                    showMessage(
                        "Geçerli bir mail adresi giriniz",
                        Toast.LENGTH_SHORT
                    )

                    et.text.clear()
                } else if (calculateStrLength(finalTexEmail) <= cannotMoreThanCharecter) {
                    showMessage(
                        "@ , - , _ , . hariç $cannotMoreThanCharecter karakterden fazla olması lazım",
                        Toast.LENGTH_SHORT
                    )
                    et.text.clear()
                }
            }
        }

        return this
    }

    /**
     * multiple @ charecters ignore
     */
    fun ignoreMultipleSpesificSymbol(iMultipleSymbol: String): VIFEmail {
        this.iMultipleSymbol = iMultipleSymbol
        return this
    }

    /**
     * ignore spesific charecters for first charecters..
     */
    fun ignoreFirstSpesificChar(iFirstSymbol: MutableList<String>): VIFEmail {
        this.iFirstSymbol = iFirstSymbol
        return this
    }

    /**
     * ignore spesific consecutive charecters..
     */
    fun ignoreConsecutiveChars(iConsecutiveChars: String): VIFEmail {
        this.iConsecutiveChars = iConsecutiveChars
        return this
    }

    /**
     * ignore two spesific consecutive charecters
     */
    fun ignoreTwoCharsConsecutive(iTwoConsecutiveChars: MutableList<String>): VIFEmail {
        this.iTwoConsecutiveChars = iTwoConsecutiveChars
        return this
    }

    //==============================================================================================
    // INTERNAL FUNC..
    /**
     * ignore multiple spesific symbol..
     */
    private fun iIgnoreMultipleSpesificSymbol(symbol: String): Boolean {
        return if (dest!!.isNotEmpty()) {
            if (dest!![dest!!.length.minus(1)].toString() == symbol) {
                true
            } else {
                dest!!.contains(symbol, ignoreCase = true)
            }
        } else {
            false
        }
    }

    /**
     * ignore two spesific consecutive charecters
     */
    private fun iIgnoreDotAndAtCharsConsecutive(str: String, symbol: String): Boolean {
        return if (str.isNotEmpty()) {
            str.contains(symbol, ignoreCase = true)
        } else {
            false
        }
    }

    //==============================================================================================
    // BRIDGE FUNCTIONS...
    /**
     * multiple spesific charecters ignore
     */
    private fun bIgnoreMultipleSpesificSymbol(): Boolean {
        if (iMultipleSymbol.isNotEmpty() && source[source.length - 1].toString() == iMultipleSymbol) {
            if (iIgnoreMultipleSpesificSymbol(source[source.length - 1].toString())) {
                showMessage(
                    "Birden fazla $iMultipleSymbol sembolü girilemez",
                    Toast.LENGTH_SHORT
                )
                return true
            }
        }

        return false
    }

    /**
     * ignore spesific charecters for first charecters..
     */
    private fun bIgnoreSpesificChar(checkMe: String): Boolean {
        if (iFirstSymbol.isNotEmpty()) {
            iFirstSymbol.forEach {
                if (it.isNotEmpty() && source.length == 1 && dend == 0 && source == it) {
                    if (iIgnoreSpesificChar(dend!!, checkMe, it)) {
                        showMessage(
                            "İlk karakter $it sembolü olamaz",
                            Toast.LENGTH_SHORT
                        )
                        return true
                    }
                }
            }
        }

        return false
    }

    /**
     * ignore spesific consecutive charecters..
     */
    private fun bIgnoreConsecutiveChars(): Boolean {
        if (iConsecutiveChars.isNotEmpty() && source == iConsecutiveChars) {
            if (iCheckConsecutiveRule(dest.toString() + source, 1, ".")) {
                showMessage(
                    "İki tane $iConsecutiveChars sembolü yanyana gelemez",
                    Toast.LENGTH_SHORT
                )
                return true
            }
        }

        return false
    }

    /**
     * ignore two spesific consecutive charecters
     */
    private fun bIgnoreTwoCharsConsecutive(): Boolean {
        if (iTwoConsecutiveChars.isNotEmpty()) {
            iTwoConsecutiveChars.forEach {
                if (iIgnoreDotAndAtCharsConsecutive(dest.toString() + source, it)) {
                    showMessage(
                        "$it sembolleri yanyana gelemez",
                        Toast.LENGTH_SHORT
                    )
                    return true
                }
            }
        }

        return false
    }

    //==============================================================================================
    // LOSE FOCUS LISTENERS FUNCS..
    /**
     * calculate str length without @ , - , _ , .
     */
    private fun calculateStrLength(str: String): Int {
        var count = 0
        str.forEach {
            when (it) {
                '@' -> {
                }
                '-' -> {
                }
                '_' -> {
                }
                '.' -> {
                }
                else -> {
                    count++
                }
            }
        }

        return count
    }

    /**
     * ignore last spesific chars..
     */
    private fun ignoreLastSpesificChar(text: String, c: String): Boolean? {
        return if (text.isNotEmpty()) {
            text[text.length.minus(1)].toString() == c
        } else
            null
    }

    /**
     * isValid email
     */
    private fun isValidEmail(target: CharSequence?): Boolean {
        return if (target == null) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

}