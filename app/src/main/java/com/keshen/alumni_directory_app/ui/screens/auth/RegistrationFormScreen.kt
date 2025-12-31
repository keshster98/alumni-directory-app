package com.keshen.alumni_directory_app.ui.screens.auth

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
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
    var bio by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column {
        OutlinedTextField(
            fullName, { fullName = it }, label = { Text("Full Name") }
        )
        OutlinedTextField(
            graduationYear, { graduationYear = it }, label = { Text("Graduation Year") }
        )
        OutlinedTextField(
            department, { department = it }, label = { Text("Department") }
        )
        OutlinedTextField(
            jobTitle, { jobTitle = it }, label = { Text("Job Title") }
        )
        OutlinedTextField(
            company, { company = it }, label = { Text("Company") }
        )
        OutlinedTextField(
            techStack, { techStack = it }, label = { Text("Tech Stack") }
        )
        OutlinedTextField(
            city, { city = it }, label = { Text("City") }
        )
        OutlinedTextField(
            country, { country = it }, label = { Text("Country") }
        )
        OutlinedTextField(
            bio, { bio = it }, label = { Text("Bio") }
        )

        Button(onClick = {
            scope.launch {
                runCatching {
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
                        bio = bio
                    )
                    Log.d("REG", "UID = ${authService.uid()}")
                    profileService.saveProfile(profile)
                }.onSuccess {
                    onCompleted()
                }.onFailure {
                    error = it.message
                }
            }
        }) {
            Text("Complete Registration")
        }

        error?.let { Text(it, color = Color.Red) }
    }
}