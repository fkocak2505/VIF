package com.fkocak.edittextfilterlab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import com.fkocak.edittextfilterlab.databinding.ActivityMainBinding
import com.fkocak.vif.VIFEmail
import com.fkocak.vif.VIFName
import com.fkocak.vif.VIFPhone

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val validationOfEmail = VIFEmail(binding.etEmail, this@MainActivity)
            .etLosesFocus(".", 4)
            .ignoreMultipleSpesificSymbol("@")
            .ignoreFirstSpesificChar(mutableListOf("@","."))
            .ignoreConsecutiveChars(".")
            .ignoreTwoCharsConsecutive(mutableListOf(".@", "@."))

        binding.etEmail.filters = arrayOf(validationOfEmail)
        
        val validationOfName = VIFName(this@MainActivity)
            .ignoreFirstSpesificChar(mutableListOf("ğ", " "))
            .ignoreConsecutiveCharByLimit(2)
            .ignoreVowelCharByLimit(3)
            .ignoreConsonantCharByLimit(4)

        binding.etName.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        binding.etName.filters = arrayOf(validationOfName)

        val validationOfPhone = VIFPhone(binding.etPhone, this@MainActivity)
            .etLoseFunction("5", "+90")

        binding.etPhone.filters = arrayOf(validationOfPhone)


    }
}