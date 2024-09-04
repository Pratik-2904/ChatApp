package com.pss_dev.chatapp.feature.auth.signup

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
fun SignUpScreen(
    navController: NavController = rememberNavController(),
) {
    val viewModel: SignUpViewModel = hiltViewModel()
    val uiState = viewModel.state.collectAsState()

    val email = remember { mutableStateOf("") }

    val name = remember { mutableStateOf("") }

    val password = remember { mutableStateOf("") }
    val cpassword = remember { mutableStateOf("") }

    val passwordTransformation = remember { mutableStateOf(true) }
    val context: Context = LocalContext.current
    LaunchedEffect(key1 = uiState.value) {
        when (uiState.value) {
            SignUpState.Error -> {
                Toast.makeText(context, "Error creating credentials", Toast.LENGTH_SHORT).show()
            }

            SignUpState.Loading -> {}
            SignUpState.Nothing -> {}
            SignUpState.Success -> {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
        }
    }

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
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text(text = "Name") },
                isError = (name.value.isNotEmpty()),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
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
                isError = password.value.length < 0 || password.value.isEmpty(),
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
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onDone = { /* Handle action when 'Done' is pressed, such as setting isError to true */ }
                )
            )

            OutlinedTextField(
                value = cpassword.value,
                onValueChange = { cpassword.value = it },
                label = { Text(text = "Confirm Password") },
                isError = password.value.length < 0 && password.value != cpassword.value,
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
                    onDone = { /* Handle action when 'Done' is pressed, such as setting isError to true */ }
                )
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .padding(vertical = 4.dp),
                onClick = {
                    viewModel.signUp(name.value, email.value, password.value)
                }) {
                Text(text = "Sign Up")
            }
            TextButton(onClick = {
                navController.popBackStack()
            }) {
                Text(text = "Already have account? Sign In")
            }
        }
    }
}
