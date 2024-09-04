package com.pss_dev.chatapp.feature.auth.signin

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.pss_dev.chatapp.R

@Preview(showBackground = true)
@Composable
fun SignInScreen(
    navController: NavController = rememberNavController(),
) {
    val viewModel: SignInViewModel = hiltViewModel()

    val uistate = viewModel.state.collectAsState()

    val email = remember { mutableStateOf("") }

    val context: Context = LocalContext.current
    LaunchedEffect(key1 = uistate.value) {
        when (uistate.value) {
            SignInStates.Error -> {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }

            SignInStates.Loading -> {}

            SignInStates.Nothing -> {}
            SignInStates.Success -> {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
    }


    val password = remember { mutableStateOf("") }

    val passwordTransformation = remember { mutableStateOf(true) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxSize()
                .safeContentPadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.chat),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .background(color = Color.Transparent)
            )

            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text(text = "Email") },
                isError = (email.value.isNotEmpty() && !email.value.matches("^[a-zA-Z0-9_.+]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$".toRegex())),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
            )

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text(text = "Password") },
                trailingIcon = {
                    IconButton(onClick = {
                        passwordTransformation.value = !passwordTransformation.value
                    }
                    ) {
                        Icon(imageVector = Icons.Default.RemoveRedEye, contentDescription = null)
                    }
                },
                visualTransformation = if (passwordTransformation.value) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                            keyboardController!!.hide()
                    }
                )
            )
            if (uistate.value == SignInStates.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .padding(vertical = 4.dp),
                    enabled = email.value.isNotEmpty() && password.value.isNotEmpty() && uistate.value == SignInStates.Nothing || uistate.value == SignInStates.Error,
                    onClick = {
                        viewModel.signIn(email.value, password.value)
                        if(uistate.value == SignInStates.Success) {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                        keyboardController!!.hide()

                    }) {
                    Text(text = "Sign In")
                }
                TextButton(onClick = {
                    navController.navigate("signup")
                }) {
                    Text(text = "Don't have account? Sign Up")
                }
            }
        }
    }

}