package com.loginapp.domain.use_cases

import android.util.Patterns

class ValidateEmail {

    fun execute(email: String): ValidationResult{
        if(email.isBlank()){
            return ValidationResult(
                false,
                "O email não pode estar vazio"
            )
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return ValidationResult(
                false,
                "O email não é valido"
            )
        }
        return ValidationResult(
            true
        )
    }
}