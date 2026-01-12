package com.keshen.alumni_directory_app.ui.screens.auth

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.keshen.alumni_directory_app.data.model.User
import com.keshen.alumni_directory_app.service.AuthService
import com.keshen.alumni_directory_app.service.UserProfileService
import kotlinx.coroutines.launch

@Composable
fun RegistrationFormScreen(
    authService: AuthService,
    profileService: UserProfileService,
    onCompleted: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var fullName by remember { mutableStateOf("") }
    var graduationYear by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var jobTitle by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var techStack by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
//    var bio by remember { mutableStateOf("") }

    var linkedIn by remember { mutableStateOf("") }
    var github by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    val email = authService.email()

    var showEmail by remember { mutableStateOf(true) }
    var showLinkedIn by remember { mutableStateOf(true) }
    var showGithub by remember { mutableStateOf(true) }
    var showPhone by remember { mutableStateOf(true) }

    var error by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            SectionTitle("Profile Information")
        }

        item {
            CardSection {
                InputField("Full Name", fullName) { fullName = it }
                InputField("Graduation Year", graduationYear) { graduationYear = it }
                InputField("Department", department) { department = it }
                InputField("Job Title", jobTitle) { jobTitle = it }
                InputField("Company", company) { company = it }
                InputField("Tech Stack", techStack) { techStack = it }
                InputField("City", city) { city = it }
                InputField("Country", country) { country = it }
            }
        }

        item {
            SectionTitle("Contact Visibility")
        }

        item {
            CardSection {
                ContactFieldWithToggle(
                    label = "Email",
                    value = email,
                    onValueChange = {},
                    enabled = false,
                    checked = showEmail,
                    onCheckedChange = { showEmail = it }
                )

                ContactFieldWithToggle(
                    label = "LinkedIn URL",
                    value = linkedIn,
                    onValueChange = { linkedIn = it },
                    checked = showLinkedIn,
                    onCheckedChange = { showLinkedIn = it }
                )

                ContactFieldWithToggle(
                    label = "GitHub URL",
                    value = github,
                    onValueChange = { github = it },
                    checked = showGithub,
                    onCheckedChange = { showGithub = it }
                )

                ContactFieldWithToggle(
                    label = "Phone",
                    value = phone,
                    onValueChange = { phone = it },
                    checked = showPhone,
                    onCheckedChange = { showPhone = it }
                )
            }
        }

        item {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    error = null
                    scope.launch {
                        runCatching {
                            graduationYear.toIntOrNull()
                                ?: error("Graduation Year must be a number")

                            val profile = User(
                                uid = authService.uid(),
                                email = authService.email(),
                                fullName = fullName,
                                graduationYear = graduationYear.toInt(),
                                department = department,
                                jobTitle = jobTitle,
                                company = company,
                                techStack = techStack,
                                city = city,
                                country = country,
                                linkedIn = linkedIn,
                                github = github,
                                contact = phone,
                                showEmail = showEmail,
                                showLinkedIn = showLinkedIn,
                                showGithub = showGithub,
                                showPhone = showPhone
                            )

                            profileService.saveProfile(profile)
                        }.onSuccess {
                            onCompleted()
                        }.onFailure {
                            error = it.message
                        }
                    }
                }
            ) {
                Text("Complete Registration")
            }
        }

        error?.let {
            item {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun ContactFieldWithToggle(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
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
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
                .padding(start = 12.dp)
                .width(52.dp)
        )
    }
}


@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = androidx.compose.material3.MaterialTheme.typography.titleMedium
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
private fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}
