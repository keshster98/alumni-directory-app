package com.keshen.alumni_directory_app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keshen.alumni_directory_app.core.utils.MessageType
import com.keshen.alumni_directory_app.core.utils.UiMessage
import com.keshen.alumni_directory_app.service.AuthService
import com.keshen.alumni_directory_app.service.UserProfileService
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@Composable
fun SignInScreen(
    authService: AuthService,
    profileService: UserProfileService,
    onSuccess: (Boolean) -> Unit,
    onSignUpClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nameOrEmail by remember { mutableStateOf("") }

    var isSigningIn by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<UiMessage?>(null) }

    // Ensures if there are any UI messages, it only shows on the screen for 2 seconds before disappearing
    LaunchedEffect(message) {
        if (message != null) {
            delay(2000)
            message = null
        }
    }

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
            Text(
                text = "Sign In",
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

            Spacer(Modifier.height(21.dp))

            message?.let { uiMessage ->
                val isError = uiMessage.type == MessageType.ERROR

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (isError) Color(0xFFFFEBEE) else Color(0xFFE8F5E9),
                            shape = MaterialTheme.shapes.medium
                        )
                        .border(
                            width = 1.dp,
                            color = if (isError) Color(0xFFD32F2F) else Color(0xFF388E3C),
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = uiMessage.text,
                        color = if (isError) Color(0xFFD32F2F) else Color(0xFF388E3C),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Button(
                enabled = !isSigningIn,
                onClick = {
                    message = null
                    isSigningIn = true

                    if (email.isBlank() || password.isBlank()) {
                        message = UiMessage(
                            text = "Email and/or password cannot be empty!",
                            type = MessageType.ERROR
                        )
                        isSigningIn = false
                        // Prevents scope.launch from running if the fields are empty by terminating the onClick function instantly
                        return@Button
                    }

                    scope.launch {
                        runCatching {
                            authService.signIn(email, password)
                            nameOrEmail = profileService.getUserDisplayName(authService.uid(), email)
                            profileService.isProfileCompleted(authService.uid())
                        }.onSuccess { completed ->
                            message = UiMessage(
                                text = "Success! Signing you in, $nameOrEmail",
                                type = MessageType.SUCCESS
                            )
                            delay(2000)
                            onSuccess(completed)
                        }.onFailure {
                            message = UiMessage(
                                text = it.message ?: "Something went wrong. Please try again.",
                                type = MessageType.ERROR
                            )
                            isSigningIn = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign In")
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = onSignUpClick) {
                Text("Don't have an account yet? Sign Up")
            }
        }
    }
}
