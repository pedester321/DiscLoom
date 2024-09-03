package com.loginapp.ui.signup

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.loginapp.R
import com.loginapp.data.model.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.Calendar


@Composable
fun SignUpScreenRoot(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    SignUpScreen(
        state = viewModel.state,
        validationEvents = viewModel.validationEvents,
        onAction = { action ->
            when (action) {
                is SignUpAction.GoTo -> navController.popBackStack()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun SignUpScreen(
    state: SignUpState,
    validationEvents: Flow<ValidationEvent>,
    onAction: (SignUpAction) -> Unit
) {
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf("") }
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
            onAction(SignUpAction.BirthDate(selectedDate))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White, // Background color when focused
        unfocusedContainerColor = Color.White, // Background color when unfocused
        focusedTextColor = Color.Black, // Text color when focused
        unfocusedTextColor = Color.Black, // Text color when unfocused
        disabledContainerColor = Color.LightGray, // Background color when disabled
        cursorColor = Color.Black, // Cursor color
        focusedIndicatorColor = Color.Black, // No underline when focused
        unfocusedIndicatorColor = Color.Black // No underline when unfocused
    )

    LaunchedEffect(key1 = context) {
        validationEvents.collect { event ->
            when (event) {
                is ValidationEvent.Success -> {
                    Toast.makeText(
                        context,
                        "Cadastro Realizado. Confirme seu email para acessar a plataforma",
                        Toast.LENGTH_LONG
                    ).show()
                    delay(3000)
                    onAction(SignUpAction.GoTo(Screen.LoginScreen))
                }

                is ValidationEvent.Failure -> {
                    Toast.makeText(
                        context,
                        event.errorMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.35f)
        )
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Column {
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = { onAction(SignUpAction.GoTo(Screen.LoginScreen)) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SkipPrevious,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(40.dp)
                            )
                            Text(
                                text = "Voltar",
                                color = Color.Black,
                                fontSize = 16.sp
                            )
                        }
                    }

                }
            },
            bottomBar = {

            },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    Surface(
                        modifier = Modifier
                            .padding(16.dp)
                            .wrapContentSize()
                            .clip(shape = RoundedCornerShape(8.dp))
                            .border(
                                width = 1.dp, // Width of the border
                                color = Color.Black, // Color of the border
                                shape = RoundedCornerShape(8.dp) // Shape with rounded corners
                            ),
                        color = Color(0xFFF7F3E8) // Cor de fundo semelhante à da imagem
                    ){
                        Column(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(16.dp),
                            Arrangement.Top,
                            Alignment.CenterHorizontally
                        ) {
                            OutlinedTextField(
                                value = state.name,
                                onValueChange = { onAction(SignUpAction.UpdateName(it)) },
                                isError = state.nameError != null,
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(text = "Nome")
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email
                                ),
                                supportingText = {
                                    state.nameError?.let { error ->
                                        Text(text = error)
                                    }
                                },
                                colors = textFieldColors
                            )
                            OutlinedTextField(
                                value = state.email,
                                onValueChange = { onAction(SignUpAction.UpdateEmail(it)) },
                                isError = state.emailError != null,
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(text = "Email")
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password
                                ),
                                supportingText = {
                                    state.emailError?.let { error ->
                                        Text(text = error)
                                    }
                                },
                                colors = textFieldColors
                            )
                            OutlinedTextField(
                                value = state.password,
                                onValueChange = { onAction(SignUpAction.UpdatePassword(it)) },
                                isError = state.passwordError != null,
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(text = "Senha")
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password
                                ),
                                visualTransformation = PasswordVisualTransformation(),
                                supportingText = {
                                    state.passwordError?.let { error ->
                                        Text(text = error)
                                    }
                                },
                                colors = textFieldColors
                            )
                            OutlinedTextField(
                                value = state.confirmPassword,
                                onValueChange = { onAction(SignUpAction.UpdateConfirmPassword(it)) },
                                isError = state.confirmPasswordError != null,
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(text = "Confirme a senha")
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email
                                ),
                                visualTransformation = PasswordVisualTransformation(),
                                supportingText = {
                                    state.confirmPasswordError?.let { error ->
                                        Text(text = error)
                                    }
                                },
                                colors = textFieldColors
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                //verticalAlignment = Alignment.CenterVertically

                            ) {
                                OutlinedTextField(
                                    value = state.birthDate,
                                    onValueChange = { },
                                    isError = state.birthDateError != null,
                                    placeholder = {
                                        Text(text = "Data de Nascimento")
                                    },
                                    modifier = Modifier.weight(1f),
                                    readOnly = true,
                                    supportingText = {
                                        state.birthDateError?.let { error ->
                                            Text(text = error)
                                        }
                                    },
                                    colors = textFieldColors
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Button(
                                    onClick = { datePickerDialog.show() },
                                    modifier = Modifier.height(56.dp),
                                    shape = RoundedCornerShape(4.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(
                                            0xFF1C1C1C
                                        )
                                    )
                                ) {
                                    Text(text = "alterar")
                                }
                            }
                            Button(
                                onClick = { onAction(SignUpAction.SignUpClicked) },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(4.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFF1C1C1C
                                    )
                                )
                            ) {
                                Text(text = "Cadastrar")
                            }
                        }

                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(
        state = SignUpState(),
        validationEvents = flowOf(),
        onAction = {}
    )
}