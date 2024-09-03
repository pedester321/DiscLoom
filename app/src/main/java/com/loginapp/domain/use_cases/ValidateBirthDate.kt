package com.loginapp.domain.use_cases

import android.util.Patterns

class ValidateBirthDate {

    fun execute(birthDate: String): ValidationResult{
        val regex = "^[a-zA-ZÀ-ÿ'\\s]+$".toRegex()

        if(birthDate.isBlank()){
            return ValidationResult(
                false,
                "Selecione uma data"
            )
        }
        return ValidationResult(
            true
        )
    }
}