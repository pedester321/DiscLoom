package com.loginapp.ui.login

import com.loginapp.data.model.Screen

sealed class LoginAction {
    data class UpdateEmail(val input: String) : LoginAction()
    data class UpdatePassword(val input: String) : LoginAction()
    data object LoginClicked : LoginAction()
    data class GoTo(val screen: Screen) : LoginAction()
}