package com.keshen.alumni_directory_app.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.keshen.alumni_directory_app.ui.navigation.Screen

@Composable
fun AdminDashboardScreen(
    navController: NavController,
    viewModel: AdminDashboardViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::onSearch,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search alumni…") }
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                DropdownFilter("Tech Stack", viewModel.techOptions.collectAsState().value,
                    viewModel.selectedTech.collectAsState().value) {
                    viewModel.selectedTech.value = it
                    viewModel.applyFilters()
                }

                DropdownFilter("Graduation Year",
                    viewModel.yearOptions.collectAsState().value.map { it.toString() },
                    viewModel.selectedYear.collectAsState().value?.toString()
                ) {
                    viewModel.selectedYear.value = it?.toInt()
                    viewModel.applyFilters()
                }

                DropdownFilter("City",
                    viewModel.cityOptions.collectAsState().value,
                    viewModel.selectedCity.collectAsState().value
                ) {
                    viewModel.selectedCity.value = it
                    viewModel.applyFilters()
                }

                DropdownFilter("Country",
                    viewModel.countryOptions.collectAsState().value,
                    viewModel.selectedCountry.collectAsState().value
                ) {
                    viewModel.selectedCountry.value = it
                    viewModel.applyFilters()
                }

                DropdownFilter(
                    label = "Status",
                    options = viewModel.statusOptions,
                    selected = viewModel.selectedStatus.collectAsState().value
                ) {
                    viewModel.selectedStatus.value = it
                    viewModel.applyFilters()
                }

                TextButton(
                    onClick = viewModel::clearFilters,
                    modifier = Modifier.align(Alignment.End)
                ) { Text("Clear filters") }
            }
        }

        when {
            isLoading -> item { Text("Loading…", textAlign = TextAlign.Center) }
            error != null -> item { Text(error!!, color = MaterialTheme.colorScheme.error) }

            else -> {
                items(users) { user ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(Screen.AdminAlumniEdit(user.uid))
                            }
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(user.fullName, style = MaterialTheme.typography.titleMedium)
                            Text("Batch ${user.graduationYear}")
                            Text(user.status.name, color = MaterialTheme.colorScheme.primary)
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