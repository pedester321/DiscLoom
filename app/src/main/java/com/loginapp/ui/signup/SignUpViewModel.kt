package com.loginapp.ui.signup

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loginapp.data.model.ApiResult
import com.loginapp.data.repository.LoginRepository
import com.loginapp.domain.use_cases.ValidateBirthDate
import com.loginapp.domain.use_cases.ValidateConfirmPassword
import com.loginapp.domain.use_cases.ValidateEmail
import com.loginapp.domain.use_cases.ValidateName
import com.loginapp.domain.use_cases.ValidatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class SignUpState(
    val name: String = "",
    val nameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val birthDate: String = "",
    val birthDateError: String? = null,
    val isLoading: Boolean = false
)

sealed class ValidationEvent {
    data object Success : ValidationEvent()
    data class Failure(val errorMessage: String?) : ValidationEvent()
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val validateName: ValidateName,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val validateConfirmPassword: ValidateConfirmPassword,
    private val validateBirthDate: ValidateBirthDate,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var state by mutableStateOf(SignUpState())
        private set

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onAction(action: SignUpAction) {
        when (action) {
            is SignUpAction.BirthDate -> updateBirthDate(action.input)
            SignUpAction.SignUpClicked -> signUpClicked()
            is SignUpAction.UpdateConfirmPassword -> updateConfirmPassword(action.input)
            is SignUpAction.UpdateEmail -> updateEmail(action.input)
            is SignUpAction.UpdateName -> updateName(action.input)
            is SignUpAction.UpdatePassword -> updatePassword(action.input)
            else -> Unit
        }
    }

    private fun updateName(input: String) {
        state = state.copy(name = input)
    }

    private fun updateEmail(input: String) {
        state = state.copy(email = input)
    }

    private fun updatePassword(input: String) {
        state = state.copy(password = input)
    }

    private fun updateConfirmPassword(input: String) {
        state = state.copy(confirmPassword = input)
    }

    private fun updateBirthDate(input: String) {
        state = state.copy(birthDate = input)
    }

    private fun updateIsLoading(input: Boolean) {
        state = state.copy(isLoading = input)
    }

    private fun signUpClicked() {
        updateIsLoading(true)
        val nameResult = validateName.execute(state.name)
        val emailResult = validateEmail.execute(state.email)
        val passwordResult = validatePassword.execute(state.password)
        val confirmPasswordResult =
            validateConfirmPassword.execute(state.password, state.confirmPassword)
        val birthDateResult = validateBirthDate.execute(state.birthDate)

        val hasError = listOf(
            nameResult,
            emailResult,
            passwordResult,
            confirmPasswordResult,
            birthDateResult
        ).any { !it.successful }

        if (hasError) {
            state = state.copy(
                nameError = nameResult.errorMessage,
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                confirmPasswordError = confirmPasswordResult.errorMessage,
                birthDateError = birthDateResult.errorMessage
            )
            updateIsLoading(false)
            return
        }

        viewModelScope.launch {
            loginRepository.signUp(
                state.name,
                state.email,
                state.password,
                formatDateString(state.birthDate)
            ).collect { result ->
                when (result) {
                    is ApiResult.Error -> {
                        updateIsLoading(false)
                        validationEventChannel.send(ValidationEvent.Failure(result.message))
                    }
                    is ApiResult.Success -> {
                        updateIsLoading(false)
                        validationEventChannel.send(ValidationEvent.Success)
                    }
                }
            }
        }
    }
}

private fun formatDateString(dateString: String): String {
    // Formato original da string
    val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    // Formato desejado
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // Analisando a string original para um objeto Date
    val date: Date = inputFormat.parse(dateString) ?: return ""
    // Formatando para o novo formato
    return outputFormat.format(date)
}

