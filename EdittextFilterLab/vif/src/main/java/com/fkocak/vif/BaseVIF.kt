package com.fkocak.vif

import android.content.Context
import android.widget.Toast
import com.fkocak.vif.interfaced.IBaseVIF

abstract class BaseVIF(open var context: Context): IBaseVIF {

    /**
     * ignore Spesific Char..
     */
    override fun iIgnoreSpesificChar(dend: Int, source: String, c: CharSequence): Boolean {
        if (dend == 0 && source.equals(c.toString(), ignoreCase = true))
            return true
        return false
    }

    /**
     * check consecutive rule for limit params..
     */
    override fun iCheckConsecutiveRule(str: String, limit: Int, specificChar: String?): Boolean {
        for (i in str.indices) {
            var count = 1
            for (j in i + 1 until str.length) {
                if (specificChar != null)
                    if (specificChar != str[i].toString())
                        break
                if ((str[i] != str[j]))
                    break

                count++
            }

            if (count > limit)
                return true

        }

        return false
    }

    /**
     * Show Message
     */
    override fun showMessage(msg: String, longFlag: Int) {
        Toast.makeText(context, msg, longFlag).show()
    }
}