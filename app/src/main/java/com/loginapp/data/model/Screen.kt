package com.loginapp.data.model

import kotlinx.serialization.Serializable

sealed class SubGraph{
    @Serializable
    data object Auth: SubGraph()

    @Serializable
    data object Home: SubGraph()
}

sealed class Screen {
    @Serializable
    data object LoginScreen: Screen()

    @Serializable
    data object SignUpScreen: Screen()

    @Serializable
    data object HomeScreen: Screen()
}
