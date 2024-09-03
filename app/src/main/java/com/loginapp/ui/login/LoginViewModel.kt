package com.loginapp.ui.login

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loginapp.data.model.ApiResult
import com.loginapp.data.model.UserSingleton
import com.loginapp.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginState(
    val email: String = "",
    val password: String = "",
    val token: String = ""
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val userSingleton: UserSingleton,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.LoginClicked -> { login() }
            is LoginAction.UpdateEmail -> { updateEmail(action.input) }
            is LoginAction.UpdatePassword -> { updatePassword(action.input) }
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

    private fun updateToken(input: String) {
        state = state.copy(token = input)
    }

    private fun login() {
        viewModelScope.launch {
            loginRepository.login(state.email, state.password).collectLatest { result ->
                when (result) {
                    is ApiResult.Error -> {
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    }

                    is ApiResult.Success -> {
                        updateToken(result.data.toString())
                        //_user.value = userSingleton.user
                    }
                }
            }
        }
    }
}