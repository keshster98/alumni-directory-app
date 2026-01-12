package com.keshen.alumni_directory_app.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.keshen.alumni_directory_app.ui.navigation.Screen

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()

    val techOptions by viewModel.techOptions.collectAsState()
    val countryOptions by viewModel.countryOptions.collectAsState()
    val yearOptions by viewModel.yearOptions.collectAsState()

    val selectedTech by viewModel.selectedTech.collectAsState()
    val selectedCountry by viewModel.selectedCountry.collectAsState()
    val selectedYear by viewModel.selectedYear.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearch(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search alumni...") },
                singleLine = true
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                DropdownFilter(
                    label = "Tech Stack",
                    options = techOptions,
                    selected = selectedTech,
                    onSelect = {
                        viewModel.selectedTech.value = it
                        viewModel.onFilterChanged()
                    }
                )

                DropdownFilter(
                    label = "Graduation Year",
                    options = yearOptions.map { it.toString() },
                    selected = selectedYear?.toString(),
                    onSelect = {
                        viewModel.selectedYear.value = it?.toInt()
                        viewModel.onFilterChanged()
                    }
                )

                DropdownFilter(
                    label = "Country",
                    options = countryOptions,
                    selected = selectedCountry,
                    onSelect = {
                        viewModel.selectedCountry.value = it
                        viewModel.onFilterChanged()
                    }
                )

                TextButton(
                    onClick = { viewModel.clearFilters() },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Clear filters")
                }
            }
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
                        text = error ?: "Error",
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    Screen.AlumniProfile(uid = user.uid)
                                )
                            }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(user.fullName, style = MaterialTheme.typography.titleMedium)
                            Text(
                                "Batch ${user.graduationYear}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownFilter(
    label: String,
    options: List<String>,
    selected: String?,
    onSelect: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected ?: "All",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("All") },
                onClick = {
                    onSelect(null)
                    expanded = false
                }
            )

            options.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onSelect(it)
                        expanded = false
                    }
                )
            }
        }
    }
}