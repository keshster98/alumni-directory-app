package com.keshen.alumni_directory_app.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
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
    var error by remember { mutableStateOf<String?>(null) }

    Column {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = {
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
        ) {
            Text("Sign Up")
        }

        error?.let { Text(it, color = Color.Red) }

        Spacer(Modifier.height(16.dp))

        TextButton(onClick = onSignInClick) {
            Text("Already have an account? Sign In")
        }
    }
}