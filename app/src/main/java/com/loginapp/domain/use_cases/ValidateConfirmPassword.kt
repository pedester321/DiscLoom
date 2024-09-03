package com.loginapp.domain.use_cases

import android.util.Patterns
import com.loginapp.ui.signup.SignUpAction

class ValidateConfirmPassword {

    fun execute(password: String, confirmPassword: String): ValidationResult{
        if(password != confirmPassword){
            return ValidationResult(
                false,
                "As senhas não coincidem"
            )
        }
        return ValidationResult(
            true
        )
    }
}