package com.loginapp.ui.home

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.loginapp.data.model.SessionManager
import com.loginapp.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

data class HomeState(
    val user: User = User(),
    val isAuthenticated: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    //@ApplicationContext private val context: Context
) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    init{
        if (sessionManager.getJwt() == null) {
            updateIsAuthenticated(false)
        }else{
            updateUser(sessionManager.decodeJwtToUser()!!)
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.LogoutClicked ->{
                sessionManager.clearJwt()
                updateIsAuthenticated(false)
            }
            else -> {}
        }
    }

    private fun updateUser(user: User) {
        state = state.copy(
            user = user
        )
    }
    private fun updateIsAuthenticated(input: Boolean) {
        state = state.copy(isAuthenticated = input)
    }
}
