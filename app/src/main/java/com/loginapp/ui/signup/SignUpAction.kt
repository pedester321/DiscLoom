package com.loginapp.ui.signup

import com.loginapp.data.model.Screen

sealed class SignUpAction {
    data class UpdateEmail(val input: String) : SignUpAction()
    data class UpdatePassword(val input: String) : SignUpAction()
    data class UpdateConfirmPassword(val input: String) : SignUpAction()
    data class UpdateName(val input: String) : SignUpAction()
    data class BirthDate(val input: String) : SignUpAction()
    data object SignUpClicked : SignUpAction()
    data class GoTo(val screen: Screen) : SignUpAction()

}