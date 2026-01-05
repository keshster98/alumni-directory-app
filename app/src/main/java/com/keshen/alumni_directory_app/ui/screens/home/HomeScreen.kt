package com.keshen.alumni_directory_app.ui.screens.home

//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.keshen.alumni_directory_app.service.AuthService
//import com.keshen.alumni_directory_app.ui.navigation.Screen
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.keshen.alumni_directory_app.R
//
//
//@Composable
//fun HomeScreen(
//    navController: NavController,
//    authService: AuthService
//) {
//    Box(modifier = Modifier.fillMaxSize()) {
//        LazyColumn(
//            verticalArrangement = Arrangement.spacedBy(16.dp),
//            contentPadding = PaddingValues(16.dp),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            item {
//                var searchQuery by remember { mutableStateOf("") }
//
//                Column {
//                    OutlinedTextField(
//                        value = searchQuery,
//                        onValueChange = { searchQuery = it },
//                        modifier = Modifier.fillMaxWidth(),
//                        placeholder = { Text("Search alumni...") },
//                        singleLine = true,
//                        leadingIcon = {
//                            Icon(
//                                painter = painterResource(id = R.drawable.ic_search),
//                                contentDescription = "Search"
//                            )
//                        },
//                        trailingIcon = {
//                            IconButton(onClick = {
//                            }) {
//                                Icon(
//                                    painter = painterResource(id = R.drawable.ic_filterlist),
//                                    contentDescription = "Filter"
//                                )
//                            }
//                        }
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//                }
//            }
//
//            item {
//                Card(
//                    elevation = CardDefaults.cardElevation(4.dp),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable {
//                            navController.navigate(Screen.AlumniProfile)
//                        }
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//
//                        Column {
//                            Text(
//                                text = "Name",
//                                style = MaterialTheme.typography.titleMedium
//                            )
//                            Text(
//                                text = "Batch",
//                                style = MaterialTheme.typography.bodyMedium
//                            )
//                        }
//                        /*
//                        if (isAdmin) {
//                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//
//                                Icon(
//                                    painter = painterResource(id = R.drawable.ic_done),
//                                    contentDescription = "Approve",
//                                    tint = MaterialTheme.colorScheme.primary,
//                                    modifier = Modifier.clickable {
//
//                                    }
//                                )
//
//                                Icon(
//                                    painter = painterResource(id = R.drawable.ic_cancel),
//                                    contentDescription = "Reject",
//                                    tint = MaterialTheme.colorScheme.error,
//                                    modifier = Modifier.clickable {
//
//                                    }
//                                )
//                            }
//                        }
//                        */
//                    }
//                }
//            }
//        }
//        Button(
//            onClick = {
//                authService.signOut()
//
//                navController.navigate(Screen.SignIn) {
//                    popUpTo(0) { inclusive = true }
//                }
//            },
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            Text("Log Out")
//        }
//    }
//}

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.keshen.alumni_directory_app.service.AuthService
import com.keshen.alumni_directory_app.ui.navigation.Screen

@Composable
fun HomeScreen(
    navController: NavController,
    authService: AuthService,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val users = viewModel.users.value
    val isLoading = viewModel.isLoading.value
    val error = viewModel.error.value

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val searchQuery = viewModel.searchQuery.value

            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearch(it) },
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                error != null -> {
                    item {
                        Text(
                            text = error ?: "Unknown error",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                users.isEmpty() -> {
                    item {
                        Text(
                            text = "No alumni found",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                else -> {
                    items(users) { user ->
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

        Button(
            onClick = {
                authService.signOut()
                navController.navigate(Screen.SignIn) {
                    popUpTo(0) { inclusive = true }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Log Out")
        }
    }
}