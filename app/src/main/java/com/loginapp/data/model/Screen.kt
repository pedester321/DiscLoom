package com.loginapp.data.model

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object LoginScreen: Screen

    @Serializable
    data object SignUpScreen: Screen

    @Serializable
    data object HomeScreen: Screen
}
