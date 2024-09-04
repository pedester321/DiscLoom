package com.loginapp.ui.home

import com.loginapp.data.model.Screen

sealed class HomeAction {
    data object LogoutClicked : HomeAction()
    //data class GoTo(val screen: Screen) : HomeAction()
    data object Logout : HomeAction()
}