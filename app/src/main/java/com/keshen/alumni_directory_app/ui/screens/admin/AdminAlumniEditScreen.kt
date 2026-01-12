package com.keshen.alumni_directory_app.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.keshen.alumni_directory_app.core.utils.MessageType
import com.keshen.alumni_directory_app.data.model.Status

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAlumniEditScreen(
    uid: String,
    navController: NavController,
    viewModel: AdminAlumniEditViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val message by viewModel.message.collectAsState()

    LaunchedEffect(uid) {
        viewModel.load(uid)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Alumni Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            user == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Profile not found")
                }
            }

            else -> {
                val edit = user!!

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    message?.let { uiMessage ->
                        item {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor =
                                        if (uiMessage.type == MessageType.ERROR)
                                            MaterialTheme.colorScheme.errorContainer
                                        else
                                            MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                Text(
                                    text = uiMessage.text,
                                    modifier = Modifier.padding(12.dp),
                                    color =
                                        if (uiMessage.type == MessageType.ERROR)
                                            MaterialTheme.colorScheme.onErrorContainer
                                        else
                                            MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }

                    item { SectionTitle("Profile Information") }
                    item {
                        CardSection {
                            EditField("Full Name", edit.fullName) {
                                viewModel.updateUser(edit.copy(fullName = it))
                            }
                            EditField("Email", edit.email, enabled = false) {}
                        }
                    }

                    item { SectionTitle("Academic Information") }
                    item {
                        CardSection {
                            EditField("Graduation Year", edit.graduationYear.toString()) {
                                viewModel.updateUser(
                                    edit.copy(graduationYear = it.toIntOrNull() ?: edit.graduationYear)
                                )
                            }
                            EditField("Department", edit.department) {
                                viewModel.updateUser(edit.copy(department = it))
                            }
                        }
                    }

                    item { SectionTitle("Work Information") }
                    item {
                        CardSection {
                            EditField("Job Title", edit.jobTitle) {
                                viewModel.updateUser(edit.copy(jobTitle = it))
                            }
                            EditField("Company", edit.company) {
                                viewModel.updateUser(edit.copy(company = it))
                            }
                            EditField("Tech Stack", edit.primaryTechStack) {
                                viewModel.updateUser(edit.copy(primaryTechStack = it))
                            }
                        }
                    }

                    item { SectionTitle("Location") }
                    item {
                        CardSection {
                            EditField("City", edit.city) {
                                viewModel.updateUser(edit.copy(city = it))
                            }
                            EditField("Country", edit.country) {
                                viewModel.updateUser(edit.copy(country = it))
                            }
                        }
                    }

                    item { SectionTitle("Account Status") }
                    item {
                        CardSection {

                            Text(
                                text = "Current Status: ${edit.status}",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(Modifier.height(8.dp))

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                StatusButton("Approve") {
                                    viewModel.updateStatus(Status.APPROVED)
                                }
                                StatusButton("Pending") {
                                    viewModel.updateStatus(Status.PENDING)
                                }
                                StatusButton("Reject") {
                                    viewModel.updateStatus(Status.REJECTED)
                                }
                                StatusButton("Deactivate") {
                                    viewModel.updateStatus(Status.INACTIVE)
                                }
                            }
                        }
                    }

                    item {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isSaving,
                            onClick = { viewModel.saveProfile() }
                        ) {
                            Text("Save Changes")
                        }
                    }
                }
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
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content
        )
    }
}

@Composable
private fun EditField(
    label: String,
    value: String,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = true
    )
}

@Composable
private fun StatusButton(
    text: String,
    onClick: () -> Unit
) {
    OutlinedButton(onClick = onClick) {
        Text(text)
    }
}
