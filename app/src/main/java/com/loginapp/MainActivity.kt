package com.loginapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.loginapp.data.model.Screen
import com.loginapp.ui.login.LoginScreenRoot
import com.loginapp.ui.signup.SignUpScreenRoot
import com.loginapp.ui.theme.LoginAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginAppTheme {
                Navigation()
            }
        }
    }
}

@Composable
fun Navigation(modifier: Modifier = Modifier){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen,
    ){
        composable<Screen.LoginScreen>{
            LoginScreenRoot(navController = navController)
        }
        composable<Screen.SignUpScreen>{
            SignUpScreenRoot(navController = navController)
        }
        composable<Screen.HomeScreen>{
            HomeScreen()
        }
    }
}

@Composable
fun HomeScreen(){
    Text(text = "Home")
}