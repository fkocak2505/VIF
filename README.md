
# VIF (Validation Input Filter)

This library allows you to add various validations for the Edittext Input you use in your application.



## Features

- Email
- Name or Surname
- Phone

Validations..

  
# Implementation
```kotlin
dependencies {
    implementation 'io.github.fkocak2505:vif:1.0.0'
}
```

# Classes & Functions

#### VIFEmail (Validation Input Filter for Email)

| Function Name | Parameters
| :-------- | :------- 
| `etLosesFocus()` | `ignoreLastSpesificChar: String,  cannotMoreThanCharecter: String`
| `ignoreMultipleSpesificSymbol()` | `iMultipleSymbol: String`
| `ignoreFirstSpesificChar()` | `iFirstSymbol: MutableList<String>`
| `ignoreConsecutiveChars()` | `iConsecutiveChars: String`
| `ignoreTwoCharsConsecutive()` | `iTwoConsecutiveChars: MutableList<String>`


#### VIFName (Validation Input Filter for Name or Surname)

| Function Name | Parameters
| :-------- | :------- 
| `ignoreFirstSpesificChar()` | `iFirstSpesificSymbol: MutableList<String>`
| `ignoreConsecutiveCharByLimit()` | `iConsecutiveChartLimit: Int`
| `ignoreVowelCharByLimit()` | `iVowelCharLimit: Int`
| `ignoreConsonantCharByLimit()` | `iConsonantCharLimit: Int`

#### VIFPhone (Validation Input Filter for Phone)

| Function Name | Parameters
| :-------- | :------- 
| `etLoseFunction()` | `ignoreFirstCharForPhoneNumber: String,  areaCode: String`


## VIFEmail Functions Description
### etLosesFocus(ignoreLastSpesificChar, cannotMoreThanCharecter)
It works as soon as the edittext loses focus. It has two parameters. 
- First parameter, you specify which last character of Email you cannot type. 
- The second parameter is **"@,-,_,."** Except for the minimum value that should be in the email. For example "**a@b.c**" would be an invalid mail.

&nbsp;
### ignoreMultipleSpesificSymbol(iMultipleSymbol)
If you do not want to use more than one special character **(without @,-,_,.)** in the e-mail address, you should use this method.

&nbsp;
### ignoreFirstSpesificChar(iFirstSymbol)
If there are characters that you do not want for the first letter in the email address, you should use this method. You can write as many values ​​as you want in the array.

&nbsp;
### ignoreConsecutiveChars(iConsecutiveChars)
If you do not want consecutive characters in the email, you should use this method.

&nbsp;
### ignoreTwoCharsConsecutive(iTwoConsecutiveChars)
If there are two characters in the email that you do not want to be consecutive, you should use this method. You must send values ​​in array
  
&nbsp;
## VIFName Functions Description
### ignoreFirstSpesificChar(iFirstSpesificSymbol)
If there are two characters in the name that you do not want to be consecutive, you should use this method. You must send values ​​in array

&nbsp;
### ignoreConsecutiveCharByLimit(iConsecutiveChartLimit)
If you want to determine the maximum number of the same character in the name, you should use this method.

&nbsp;
### ignoreVowelCharByLimit(iVowelCharLimit)
If you want to specify the maximum number of repetitions of vowels in the name, you should use this method.

&nbsp;
### ignoreConsonantCharByLimit(iConsonantCharLimit)
If you want to specify the maximum number of consonants in the name, you should use this method.

&nbsp;
## VIFPhone Functions Description
### etLoseFunction(ignoreFirstCharForPhoneNumber,areaCode)
It works as soon as the edittext loses focus. It has two parameters. 
- First parameter, you specify which first number of phone you cannot type. 
- The second parameter is area code. **This parameter is mandatory to calculate the first parameter and must be written in full.**

&nbsp;
## VIF Example
### VIFEmail 
```kotlin
VIFEmail(binding.etEmail, this@MainActivity)
        .etLosesFocus(".", 4)
        .ignoreMultipleSpesificSymbol("@")
        .ignoreFirstSpesificChar(mutableListOf("@","."))
        .ignoreConsecutiveChars(".")
        .ignoreTwoCharsConsecutive(mutableListOf(".@", "@."))

binding.etEmail.filters = arrayOf(validationOfEmail)
```

&nbsp;
### VIFName 
```kotlin
VIFName(this@MainActivity)
        .ignoreFirstSpesificChar(mutableListOf("ğ", " "))
        .ignoreConsecutiveCharByLimit(2)
        .ignoreVowelCharByLimit(3)
        .ignoreConsonantCharByLimit(4)

binding.etName.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
binding.etName.filters = arrayOf(validationOfName)
```

&nbsp;
### VIFPhone 
```kotlin
VIFPhone(binding.etPhone, this@MainActivity)
        .etLoseFunction("5", "+90")

binding.etPhone.filters = arrayOf(validationOfPhone)
```

&nbsp;
## Authors

- [@fkocak2505](https://www.linkedin.com/in/fatih-ko%C3%A7ak-36868691/)

  