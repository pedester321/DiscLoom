package com.loginapp.ui.login

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.loginapp.R
import com.loginapp.data.model.Screen


@Composable
fun LoginScreenRoot(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    LoginScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is LoginAction.GoTo -> navController.navigate(action.screen)
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit
) {

    LaunchedEffect(key1 = state.isAuthenticated) {
        if (state.isAuthenticated) {
            onAction(LoginAction.GoTo(Screen.HomeScreen))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
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

            },
            bottomBar = {

            },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 32.dp)
                            .fillMaxWidth(),
                        //horizontalAlignment = Alignment.Start
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            Arrangement.Center,
                            Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(R.drawable.logo),
                                contentDescription = "logo",
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "Disc Loom",
                                fontFamily = FontFamily.Cursive,
                                fontSize = 60.sp
                            )
                        }

                        Surface(
                            modifier = Modifier
                                .wrapContentSize()
                                .clip(shape = RoundedCornerShape(8.dp))
                                .border(
                                    width = 1.dp, // Width of the border
                                    color = Color.Black, // Color of the border
                                    shape = RoundedCornerShape(8.dp) // Shape with rounded corners
                                ),
                            color = Color(0xFFF7F3E8) // Cor de fundo semelhante à da imagem
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {

                                Text("Email", fontSize = 20.sp)

                                OutlinedTextField(
                                    //label = { Text("Email") },
                                    value = state.email,
                                    onValueChange = { onAction(LoginAction.UpdateEmail(it.trim())) },
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    //.height(50.dp),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.White, // Background color when focused
                                        unfocusedContainerColor = Color.White, // Background color when unfocused
                                        focusedTextColor = Color.Black, // Text color when focused
                                        unfocusedTextColor = Color.Black, // Text color when unfocused
                                        disabledContainerColor = Color.LightGray, // Background color when disabled
                                        cursorColor = Color.Black, // Cursor color
                                        focusedIndicatorColor = Color.Black, // No underline when focused
                                        unfocusedIndicatorColor = Color.Black // No underline when unfocused
                                    )
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text("Password", fontSize = 20.sp)

                                OutlinedTextField(
                                    value = state.password,
                                    onValueChange = { onAction(LoginAction.UpdatePassword(it.trim())) },
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    singleLine = true,
                                    visualTransformation = PasswordVisualTransformation(),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.White, // Background color when focused
                                        unfocusedContainerColor = Color.White, // Background color when unfocused
                                        focusedTextColor = Color.Black, // Text color when focused
                                        unfocusedTextColor = Color.Black, // Text color when unfocused
                                        disabledContainerColor = Color.LightGray, // Background color when disabled
                                        cursorColor = Color.Black, // Cursor color
                                        focusedIndicatorColor = Color.Black, // No underline when focused
                                        unfocusedIndicatorColor = Color.Black // No underline when unfocused
                                    )
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = { onAction(LoginAction.LoginClicked) },
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(4.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(
                                            0xFF1C1C1C
                                        )
                                    ),
                                    enabled = !state.isLoading
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        if (state.isLoading) {
                                            CircularProgressIndicator(
                                                modifier = Modifier
                                                    .size(24.dp), // Set a fixed size for the indicator
                                                strokeWidth = 2.dp
                                            )
                                        } else {
                                            Text("Sign In", color = Color.White, fontSize = 18.sp)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Forgot password?",
                                    color = Color(0xFF1C1C1C),
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                )
                            }
                        }
                        //Fim da surface
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { onAction(LoginAction.GoTo(Screen.SignUpScreen)) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent // Definindo o fundo transparente, ou você pode escolher outra cor
                            ),
                            elevation = ButtonDefaults.buttonElevation(0.dp), // Sem elevaçã
                            enabled = !state.isLoading
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Ícone
                                Icon(
                                    painter = painterResource(id = R.drawable.addperson),
                                    contentDescription = null, // Descrição do conteúdo para acessibilidade
                                    tint = Color.Black, // Cor do ícone
                                    modifier = Modifier.size(40.dp) // Tamanho do ícone
                                )
                                // Texto
                                Text(
                                    text = "Sign Up", // Texto do botão
                                    color = Color.Black, // Cor do texto
                                    fontSize = 16.sp // Tamanho da fonte
                                )
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
private fun LoginScreenPreview() {
    LoginScreen(
        state = LoginState(
            isLoading = false
        ),
        onAction = {}
    )
}