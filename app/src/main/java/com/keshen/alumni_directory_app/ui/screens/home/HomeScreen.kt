package com.keshen.alumni_directory_app.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val users = viewModel.users.value
    val isLoading = viewModel.isLoading.value
    val error = viewModel.error.value
    val searchQuery = viewModel.searchQuery.value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::onSearch,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search alumni...") },
                singleLine = true
            )
        }

        when {
            isLoading -> {
                item {
                    Text(
                        text = "Loading alumni...",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }

            error != null -> {
                item {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }

            users.isEmpty() -> {
                item {
                    Text(
                        text = "No alumni found",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }

            else -> {
                items(users) { user ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = user.fullName,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Batch ${user.graduationYear}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}