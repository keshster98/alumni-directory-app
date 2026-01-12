package com.keshen.alumni_directory_app.ui.screens.status

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.keshen.alumni_directory_app.data.model.Status
import com.keshen.alumni_directory_app.service.AuthService

@Composable
fun StatusScreen(
    status: Status,
    authService: AuthService,
    onLogout: () -> Unit
) {
    val message = when (status) {
        Status.PENDING ->
            "Your account is pending confirmation by the admin team.\n\n" + "Approval usually takes 2–3 days.\n\n" + "If it takes longer, please contact admin@gmail.com."

        Status.INACTIVE ->
            "Sorry, this account is currently deactivated.\n\n" + "Please reach out to admin@gmail.com for further information."

        Status.REJECTED ->
            "Sorry, this account is rejected from joining the app.\n\n" + "Please reach out to admin@gmail.com for further information."

        else -> ""
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Account Status",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = {
                authService.signOut()
                onLogout()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Text("Log Out")
        }
    }
}
