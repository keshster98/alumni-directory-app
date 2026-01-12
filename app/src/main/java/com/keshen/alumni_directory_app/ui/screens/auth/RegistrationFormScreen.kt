package com.keshen.alumni_directory_app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.keshen.alumni_directory_app.core.utils.MessageType
import com.keshen.alumni_directory_app.data.model.User
import com.keshen.alumni_directory_app.service.AuthService

@Composable
fun RegistrationFormScreen(
    authService: AuthService,
    onCompleted: () -> Unit,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    var fullName by remember { mutableStateOf("") }
    val email = authService.email()
    var phone by remember { mutableStateOf("") }
    var showPhone by remember { mutableStateOf(true) }

    var graduationYear by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }

    var jobTitle by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var primaryTechStack by remember { mutableStateOf("") }

    var city by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }

    var linkedIn by remember { mutableStateOf("") }
    var showLinkedIn by remember { mutableStateOf(true) }
    var github by remember { mutableStateOf("") }
    var showGithub by remember { mutableStateOf(true) }

    val isSubmitting by viewModel.isSubmitting.collectAsState()
    val message by viewModel.message.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { SectionTitle("Profile Information") }
        item {
            CardSection {
                Field(
                    label = "Full Name",
                    value = fullName,
                    onValueChange = { fullName = it }
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = {},
                    label = { Text("Email") },
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                )

                Field(
                    label = "Phone",
                    value = phone,
                    onValueChange = { phone = it }
                )
            }
        }

        item { SectionTitle("Academic Information") }
        item {
            CardSection {
                Field(
                    label = "Graduation Year",
                    value = graduationYear,
                    onValueChange = { graduationYear = it }
                )

                Field(
                    label = "Department",
                    value = department,
                    onValueChange = { department = it }
                )
            }
        }

        item { SectionTitle("Work Information") }
        item {
            CardSection {
                Field(
                    label = "Job Title",
                    value = jobTitle,
                    onValueChange = { jobTitle = it }
                )
                Field(
                    label = "Company",
                    value = company,
                    onValueChange = { company = it }
                )
                Field(
                    label = "Primary Tech Stack",
                    value = primaryTechStack,
                    onValueChange = { primaryTechStack = it }
                )
            }
        }

        item { SectionTitle("Location") }
        item {
            CardSection {
                Field(
                    label = "City",
                    value = city,
                    onValueChange = { city = it }
                )
                Field(
                    label = "Country",
                    value = country,
                    onValueChange = { country = it }
                )
            }
        }

        item { SectionTitle("Online Presence") }
        item {
            CardSection {
                Text(
                    text = "Insert the official links to your accounts below.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Field(
                    label = "LinkedIn",
                    value = linkedIn,
                    onValueChange = { linkedIn = it },
                )

                Field(
                    label = "GitHub",
                    value = github,
                    onValueChange = { github = it },
                )
            }
        }

        item { SectionTitle("Data Visibility") }
        item {
            CardSection {
                Text(
                    text = "Email cannot be hidden as it is the default contact option.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                VisibilityToggle(
                    label = "Show Phone",
                    checked = showPhone,
                    onCheckedChange = { showPhone = it }
                )

                VisibilityToggle(
                    label = "Show LinkedIn",
                    checked = showLinkedIn,
                    onCheckedChange = { showLinkedIn = it }
                )

                VisibilityToggle(
                    label = "Show GitHub",
                    checked = showGithub,
                    onCheckedChange = { showGithub = it }
                )
            }
        }

        item {
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
        }

        item {
            Spacer(Modifier.height(16.dp))

            Button(
                enabled = !isSubmitting,
                onClick = {
                    val year = graduationYear.toIntOrNull() ?: 0

                    val user = User(
                        uid = authService.uid(),
                        fullName = fullName,
                        email = email,
                        phone = phone,
                        showPhone = showPhone,
                        graduationYear = year,
                        department = department,
                        jobTitle = jobTitle,
                        company = company,
                        primaryTechStack = primaryTechStack,
                        city = city,
                        country = country,
                        linkedIn = linkedIn,
                        showLinkedIn = showLinkedIn,
                        github = github,
                        showGithub = showGithub,
                        completed = true
                    )

                    viewModel.submitRegistration(user, onCompleted)
                }
            )
            {
                Text("Submit Registration")
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
private fun CardSection(content: @Composable ColumnScope.() -> Unit) {
    androidx.compose.material3.Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content
        )
    }
}

@Composable
private fun Field(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.weight(1f),
            singleLine = true,
            enabled = enabled
        )
    }
}

@Composable
private fun VisibilityToggle(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
