package com.loginapp.data.model

data class User(
    val id: Int = 0,
    val name: String = "",
    val email: String = "",
    val birthDate: String = "",
    val emailConfirmed: Boolean = false,
    val role: String = "",
)

