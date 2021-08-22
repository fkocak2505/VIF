package com.fkocak.vif

import android.content.Context
import android.text.InputFilter
import android.text.Spanned
import android.widget.Toast
import java.util.regex.Pattern

class VIFName(override var context: Context) : InputFilter, BaseVIF(context) {

    private var source: String = ""
    private var start: Int? = null
    private var end: Int? = null
    private var dest: Spanned? = null
    private var dstart: Int? = null
    private var dend: Int? = null

    var isBlock = false

    private var iFirstSpesificSymbol = mutableListOf<String>()
    private var iConsecutiveChartLimit = -1
    private var iVowelCharLimit = -1
    private var iConsonantCharLimit = -1

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

        isBlock = false

        for (i in start until end) {

            val regex = "^[a-zA-ZğüşöçıİĞÜŞÖÇ ]+\$"

            val checkMe = source?.get(i).toString()
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(checkMe)

            /**
             * check regex..
             */
            when (matcher.matches()) {
                false -> {
                    showMessage(
                        "İsim ya da soyisim alanında ingilizce karakterler ile türkçe karakterler haricinde bir karakter kullanılamaz",
                        Toast.LENGTH_SHORT
                    )
                    return source?.dropLast(1)
                }
            }

            if (bIgnoreFirstSpesificChar(checkMe))
                this.source = this.source.drop(1)

            if (bIgnoreConsecutiveCharByLimit(checkMe))
                return source?.dropLast(1)

            if (bIgnoreVowelCharByLimit(checkMe))
                return source?.dropLast(1)

            if (bIgnoreConsonantCharByLimit(checkMe))
                return source?.dropLast(1)

        }

        return this.source
    }

    //==============================================================================================
    // ACCESSING ACTIVITY OR FRAGMENT
    /**
     * Igonore spesific charecter started
     */
    fun ignoreFirstSpesificChar(iFirstSpesificSymbol: MutableList<String>): VIFName {
        this.iFirstSpesificSymbol = iFirstSpesificSymbol
        return this
    }

    /**
     * ignore consecutive charecter more than iConsecutiveChartLimit
     */
    fun ignoreConsecutiveCharByLimit(iConsecutiveChartLimit: Int): VIFName {
        this.iConsecutiveChartLimit = iConsecutiveChartLimit
        return this
    }

    /**
     * ignore vowel char by limit
     */
    fun ignoreVowelCharByLimit(iVowelCharLimit: Int): VIFName {
        this.iVowelCharLimit = iVowelCharLimit
        return this
    }

    /**
     * ignore consonant char by limit
     */
    fun ignoreConsonantCharByLimit(iConsonantCharLimit: Int): VIFName {
        this.iConsonantCharLimit = iConsonantCharLimit
        return this
    }

    //==============================================================================================
    // BRIDGE FUNCTIONS...
    private fun bIgnoreFirstSpesificChar(checkMe: String): Boolean {
        if (iFirstSpesificSymbol.isNotEmpty()) {
            iFirstSpesificSymbol.forEach {
                if (iIgnoreSpesificChar(
                        dend!!, checkMe, it
                    )
                ) {
                    showMessage(
                        "İlk karakter ${it.uppercase()} ya da $it karakteri olamaz",
                        Toast.LENGTH_SHORT
                    )
                    isBlock = true
                    return true
                }
            }
        }

        return false
    }

    private fun bIgnoreConsecutiveCharByLimit(checkMe: String): Boolean {
        if (iConsecutiveChartLimit != -1 && checkLengthIsOne(checkMe) && !isBlock) {
            if (iCheckConsecutiveRule(dest.toString() + source, iConsecutiveChartLimit)) {
                showMessage(
                    "$iConsecutiveChartLimit adetten fazla aynı karakter kullanılamaz",
                    Toast.LENGTH_SHORT
                )
                isBlock = true
                return true
            }

        } else {
            if (iCheckConsecutiveRule(source, iConsecutiveChartLimit) && !isBlock) {
                showMessage(
                    "$iConsecutiveChartLimit adetten fazla aynı karakter kullanılamaz",
                    Toast.LENGTH_SHORT
                )
                isBlock = true
                return true
            }

        }

        return false
    }

    private fun bIgnoreVowelCharByLimit(checkMe: String): Boolean {
        if (iVowelCharLimit != -1 && checkLengthIsOne(checkMe) && !isBlock) {
            if (checkConsecutiveRule4Vowel(dest.toString() + source) && !isBlock) {
                showMessage(
                    "$iVowelCharLimit adetten fazla ünlü harf yanyana kullanılamaz",
                    Toast.LENGTH_SHORT
                )
                isBlock = true
                return true
            }
        } else {
            if (checkConsecutiveRule4Vowel(source) && !isBlock) {
                showMessage(
                    "$iVowelCharLimit adetten fazla ünlü harf yanyana kullanılamaz",
                    Toast.LENGTH_SHORT
                )
                isBlock = true
                return true
            }
        }

        return false
    }

    private fun bIgnoreConsonantCharByLimit(checkMe: String): Boolean {
        if (iConsonantCharLimit != -1 && checkLengthIsOne(checkMe) && !isBlock) {
            if (checkConsecutiveRule4Consonant(dest.toString() + source) && !isBlock) {
                showMessage(
                    "$iConsonantCharLimit adetten fazla ünsüz harf yanyana kullanılamaz",
                    Toast.LENGTH_SHORT
                )
                isBlock = true
                return true
            }
        } else {
            if (checkConsecutiveRule4Consonant(source) && !isBlock) {
                showMessage(
                    "$iConsonantCharLimit adetten fazla ünsüz harf yanyana kullanılamaz",
                    Toast.LENGTH_SHORT
                )
                isBlock = true
                return true
            }
        }

        return false
    }

    //==============================================================================================
    // INTERNAL FUNC..
    /**
     * check Turkish chars for user input letter..
     */
    private fun checkLengthIsOne(source: CharSequence?): Boolean {
        return source?.length == 1
    }

    /**
     * Check consecutive Rule 4 Vowel charecters
     */
    private fun checkConsecutiveRule4Vowel(str: String): Boolean {
        for (i in str.indices) {
            var count = 1

            if (!isVowel(str[i].lowercaseChar()))
                continue

            for (j in i + 1 until str.length) {
                if (!isVowel(str[j].lowercaseChar()))
                    break
                count++
            }

            if (count > iVowelCharLimit)
                return true

        }

        return false

    }

    /**
     * check consecutive Rule 4 Consonant
     */
    private fun checkConsecutiveRule4Consonant(str: String): Boolean {

        for (i in str.indices) {
            var count = 1

            if (str[i] == ' ')
                continue

            if (isVowel(str[i].lowercaseChar()))
                continue

            for (j in i + 1 until str.length) {
                if (str[j] == ' ')
                    continue

                if (isVowel(str[j].lowercaseChar()))
                    break
                count++
            }

            if (count > iConsonantCharLimit)
                return true

        }

        return false

    }

    /**
     * charecters isVowel..
     */
    private fun isVowel(char: Char): Boolean {
        return (char == 'a') or (char == 'e') or (char == 'ı') or (char == 'i') or (char == 'o') or (char == 'ö') or (char == 'u') or (char == 'ü')
    }
}