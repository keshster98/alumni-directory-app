package com.keshen.alumni_directory_app.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keshen.alumni_directory_app.service.AuthService
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    authService: AuthService,
    onSuccess: () -> Unit,
    onSignInClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔹 Title
            Text(
                text = "Sign Up",
                fontSize = 24.sp,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    when {
                        email.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                            error = "All fields are required"
                        }
                        password != confirmPassword -> {
                            error = "Passwords do not match"
                        }
                        else -> {
                            scope.launch {
                                runCatching {
                                    authService.signUp(email, password)
                                }.onSuccess {
                                    onSuccess()
                                }.onFailure {
                                    error = it.message
                                }
                            }
                        }
                    }
                }
            ) {
                Text("Sign Up")
            }

            error?.let {
                Spacer(Modifier.height(12.dp))
                Text(it, color = Color.Red)
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = onSignInClick) {
                Text("Already have an account? Sign In")
            }
        }
    }
}