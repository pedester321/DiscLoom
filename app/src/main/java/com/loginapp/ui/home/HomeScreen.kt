package com.loginapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.loginapp.data.model.SubGraph

@Composable
fun HomeScreenRoot(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
){
    HomeScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is HomeAction.Logout -> {
                    navController.navigate(SubGraph.Auth){
                        popUpTo(SubGraph.Home) { inclusive = true }
                    }
                }
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit
){
    LaunchedEffect(key1 = state.isAuthenticated){
        if (!state.isAuthenticated){
            onAction(HomeAction.Logout)
        }
    }

    Scaffold(
        topBar = {},
        bottomBar = {},
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    Arrangement.Center,
                    Alignment.CenterHorizontally
                ){
                    Text(text = "Home Screen", fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = state.user.toString())
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { onAction(HomeAction.LogoutClicked) }) {
                        Text(text = "Logout")
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        state = HomeState(),
        {}
    )
}