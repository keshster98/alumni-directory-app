package com.keshen.alumni_directory_app.ui.screens.profile

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlumniProfileScreen(
    uid: String,
    navController: NavController,
    viewModel: AlumniProfileViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(uid) {
        viewModel.load(uid)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Alumni Profile") },
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

            error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(error!!)
                }
            }

            user != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    item { SectionTitle("Profile Information") }
                    item {
                        CardSection {
                            InfoRow("Full Name", user!!.fullName)
                            InfoRow("Email", user!!.email)
                        }
                    }

                    item { SectionTitle("Academic Information") }
                    item {
                        CardSection {
                            InfoRow("Graduation Year", user!!.graduationYear.toString())
                            InfoRow("Department", user!!.department)
                        }
                    }

                    item { SectionTitle("Work Information") }
                    item {
                        CardSection {
                            InfoRow("Job Title", user!!.jobTitle)
                            InfoRow("Company", user!!.company)
                            InfoRow("Tech Stack", user!!.primaryTechStack)
                        }
                    }

                    item { SectionTitle("Location") }
                    item {
                        CardSection {
                            InfoRow("City", user!!.city)
                            InfoRow("Country", user!!.country)
                        }
                    }

                    item { SectionTitle("Contact") }
                    item {
                        CardSection {
                            ContactIconRow(
                                email = user!!.email,
                                phone = user!!.phone.takeIf { user!!.showPhone },
                                linkedin = user!!.linkedIn.takeIf { user!!.showLinkedIn },
                                github = user!!.github.takeIf { user!!.showGithub }
                            )
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
fun ContactIconRow(
    email: String,
    phone: String?,
    linkedin: String?,
    github: String?
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        // 📧 Email (always visible)
        IconButton(onClick = {
            val intent = Intent(
                Intent.ACTION_SENDTO,
                Uri.parse("mailto:$email")
            )
            context.startActivity(intent)
        }) {
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = "Email"
            )
        }

        // 📞 Phone
        phone?.let {
            IconButton(onClick = {
                val intent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:$it")
                )
                context.startActivity(intent)
            }) {
                Icon(
                    imageVector = Icons.Filled.Phone,
                    contentDescription = "Phone"
                )
            }
        }

        // 🔗 LinkedIn
        linkedin?.let {
            IconButton(onClick = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(it)
                )
                context.startActivity(intent)
            }) {
                Icon(
                    imageVector = Icons.Filled.Link,
                    contentDescription = "LinkedIn"
                )
            }
        }

        // 🐙 GitHub
        github?.let {
            IconButton(onClick = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(it)
                )
                context.startActivity(intent)
            }) {
                Icon(
                    imageVector = Icons.Filled.Link,
                    contentDescription = "GitHub"
                )
            }
        }
    }
}

