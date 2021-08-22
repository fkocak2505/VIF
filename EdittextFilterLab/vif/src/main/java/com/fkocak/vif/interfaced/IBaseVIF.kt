package com.fkocak.vif.interfaced

interface IBaseVIF {

    fun iIgnoreSpesificChar(dend: Int, source: String, c: CharSequence): Boolean

    fun iCheckConsecutiveRule(str: String, limit: Int, specificChar: String? = null): Boolean

    fun showMessage(msg: String, longFlag: Int)

}