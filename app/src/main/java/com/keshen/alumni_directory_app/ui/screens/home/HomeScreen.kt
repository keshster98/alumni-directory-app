package com.keshen.alumni_directory_app.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.keshen.alumni_directory_app.R
import com.keshen.alumni_directory_app.ui.navigation.Screen


@Composable
fun HomeScreen(
    navController: NavController,
    isAdmin: Boolean
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                var searchQuery by remember { mutableStateOf("") }

                Column {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search alumni...") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = "Search"
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                // TODO: open filter bottom sheet / dialog
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_filterlist),
                                    contentDescription = "Filter"
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item {
                Card(
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Screen.AlumniProfile)
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column {
                            Text(
                                text = "Name",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Batch",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        if (isAdmin) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                                Icon(
                                    painter = painterResource(id = R.drawable.ic_done),
                                    contentDescription = "Approve",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.clickable {

                                    }
                                )

                                Icon(
                                    painter = painterResource(id = R.drawable.ic_cancel),
                                    contentDescription = "Reject",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.clickable {

                                    }
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}