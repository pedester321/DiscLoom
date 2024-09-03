package com.loginapp.domain.use_cases

import android.util.Patterns

class ValidateName {

    fun execute(name: String): ValidationResult{
        val regex = "^[a-zA-ZÀ-ÿ'\\s]+$".toRegex()

        if(name.isBlank()){
            return ValidationResult(
                false,
                "O Nome não pode estar vazio"
            )
        }
        if(name.length < 2){
            return ValidationResult(
                false,
                "O nome precisa ter pelo menos 2 caracteres"
            )
        }
        if(!name.matches(regex)){
            return ValidationResult(
                false,
                "O nome só pode conter letras"
            )
        }
        return ValidationResult(
            true
        )
    }
}