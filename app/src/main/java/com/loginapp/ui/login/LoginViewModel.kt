package com.loginapp.ui.login

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loginapp.data.model.ApiResult
import com.loginapp.data.model.SessionManager
import com.loginapp.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    //private val userSingleton: UserSingleton,
    private val sessionManager: SessionManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    init{
        val token = sessionManager.getJwt()
        if (token != null) {
            updateIsAuthenticated(true)
        }
    }


    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.LoginClicked -> {
                login()
            }

            is LoginAction.UpdateEmail -> {
                updateEmail(action.input)
            }

            is LoginAction.UpdatePassword -> {
                updatePassword(action.input)
            }

            else -> {}
        }
    }

//    private val _user = MutableStateFlow<User>(User())
//    val user = _user.asStateFlow()

    private fun updateEmail(input: String) {
        state = state.copy(email = input)
    }

    private fun updatePassword(input: String) {
        state = state.copy(password = input)
    }

    private fun updateIsLoading(input: Boolean) {
        state = state.copy(isLoading = input)
    }

    private fun updateIsAuthenticated(input: Boolean) {
        state = state.copy(isAuthenticated = input)
    }

    private fun login() {
        viewModelScope.launch {
            updateIsLoading(true)
            loginRepository.login(state.email, state.password).collectLatest { result ->
                when (result) {
                    is ApiResult.Error -> {
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        updateIsLoading(false)
                    }

                    is ApiResult.Success -> {
                        if (result.data != null) {
                            sessionManager.saveJwt(result.data)
                            updateIsLoading(false)
                            updateIsAuthenticated(true)
                        }
                    }
                }
            }
        }
    }
}
