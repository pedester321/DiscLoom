package com.loginapp.domain.use_cases

import android.util.Patterns

class ValidatePassword {

    fun execute(password: String): ValidationResult{
        if(password.length < 6){
            return ValidationResult(
                false,
                "A senha precisa ter pelo menos 6 caracteres"
            )
        }
        return ValidationResult(
            true
        )
    }
}