package com.keshen.alumni_directory_app.ui.screens.profile

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.keshen.alumni_directory_app.core.utils.MessageType
import com.keshen.alumni_directory_app.data.model.User

@Composable
fun UserProfileScreen(
    viewModel: UserProfileViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val message by viewModel.message.collectAsState()
    val context = LocalContext.current

    var edit by remember(user) { mutableStateOf(user) }

    when {
        isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }

        edit == null -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            Text("Profile not found")
        }

        else -> LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            message?.let {
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor =
                                if (it.type == MessageType.ERROR)
                                    MaterialTheme.colorScheme.errorContainer
                                else
                                    MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            it.text,
                            modifier = Modifier.padding(12.dp),
                            color =
                                if (it.type == MessageType.ERROR)
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
                    InfoRow("Full Name", edit!!.fullName)
                    InfoRow("Email", edit!!.email)
                    InfoRow("Graduation Year", edit!!.graduationYear.toString())
                    InfoRow("Department", edit!!.department)
                }
            }

            item { SectionTitle("Academic Information") }
            item {
                CardSection {
                    InfoRow("Full Name", edit!!.fullName)
                    InfoRow("Email", edit!!.email)
                    InfoRow("Graduation Year", edit!!.graduationYear.toString())
                    InfoRow("Department", edit!!.department)
                }
            }

            item {
                EditableSection(
                    title = "Work Information",
                    canSave = viewModel.isWorkDirty(edit!!) && !isSaving,
                    onSave = { viewModel.saveWork(edit!!) }
                ) {
                    EditField("Job Title", edit!!.jobTitle) { edit = edit!!.copy(jobTitle = it) }
                    EditField("Company", edit!!.company) { edit = edit!!.copy(company = it) }
                    EditField("Tech Stack", edit!!.primaryTechStack) {
                        edit = edit!!.copy(primaryTechStack = it)
                    }
                }
            }

            item {
                EditableSection(
                    title = "Location",
                    canSave = viewModel.isLocationDirty(edit!!) && !isSaving,
                    onSave = { viewModel.saveLocation(edit!!) }
                ) {
                    EditField("City", edit!!.city) { edit = edit!!.copy(city = it) }
                    EditField("Country", edit!!.country) { edit = edit!!.copy(country = it) }
                }
            }

            item {
                EditableSection(
                    title = "Contact",
                    canSave =
                        (viewModel.isContactDirty(edit!!) ||
                                viewModel.isVisibilityDirty(edit!!)) && !isSaving,
                    onSave = {
                        viewModel.saveContact(edit!!)
                        viewModel.saveVisibility(edit!!)
                    }
                ) {
                    ContactIconRow(edit!!, context)
                    EditField("Phone", edit!!.phone) { edit = edit!!.copy(phone = it) }
                    EditField("LinkedIn", edit!!.linkedIn) { edit = edit!!.copy(linkedIn = it) }
                    EditField("GitHub", edit!!.github) { edit = edit!!.copy(github = it) }

                    VisibilityToggle("Show phone", edit!!.showPhone) {
                        edit = edit!!.copy(showPhone = it)
                    }
                    VisibilityToggle("Show LinkedIn", edit!!.showLinkedIn) {
                        edit = edit!!.copy(showLinkedIn = it)
                    }
                    VisibilityToggle("Show GitHub", edit!!.showGithub) {
                        edit = edit!!.copy(showGithub = it)
                    }
                }
            }
        }
    }
}

/* ───────────────────────── UI HELPERS ───────────────────────── */

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
private fun CardSection(
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
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
private fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun EditableSection(
    title: String,
    canSave: Boolean,
    onSave: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                TextButton(
                    enabled = canSave,
                    onClick = onSave
                ) {
                    Text("Save")
                }
            }

            content()
        }
    }
}

@Composable
private fun EditField(
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

@Composable
fun ContactIconRow(
    user: User,
    context: android.content.Context
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        // 📧 Email (always visible)
        IconButton(
            onClick = {
                val intent = Intent(
                    Intent.ACTION_SENDTO,
                    Uri.parse("mailto:${user.email}")
                )
                context.startActivity(intent)
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = "Email"
            )
        }

        // 📞 Phone
        if (user.showPhone && user.phone.isNotBlank()) {
            IconButton(
                onClick = {
                    val intent = Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse("tel:${user.phone}")
                    )
                    context.startActivity(intent)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Phone,
                    contentDescription = "Phone"
                )
            }
        }

        // 🔗 LinkedIn
        if (user.showLinkedIn && user.linkedIn.isNotBlank()) {
            IconButton(
                onClick = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(user.linkedIn)
                    )
                    context.startActivity(intent)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Link,
                    contentDescription = "LinkedIn"
                )
            }
        }

        // 🐙 GitHub
        if (user.showGithub && user.github.isNotBlank()) {
            IconButton(
                onClick = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(user.github)
                    )
                    context.startActivity(intent)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Link,
                    contentDescription = "GitHub"
                )
            }
        }
    }
}




