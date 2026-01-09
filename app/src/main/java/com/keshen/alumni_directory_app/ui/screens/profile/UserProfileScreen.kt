package com.keshen.alumni_directory_app.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.keshen.alumni_directory_app.data.model.User

@Composable
fun UserProfileScreen(
    user: User,
    viewerUid: String,
    viewerIsAdmin: Boolean,
    onSignOut: () -> Unit
) {
    val displayEmail = user.displayEmail(viewerIsAdmin = viewerIsAdmin, viewerUid = viewerUid)
    val displayPhone = user.displayPhone(viewerIsAdmin = viewerIsAdmin, viewerUid = viewerUid)
    val displayLinkedIn = user.displayLinkedIn(viewerIsAdmin = viewerIsAdmin, viewerUid = viewerUid)
    val displayGithub = user.displayGithub(viewerIsAdmin = viewerIsAdmin, viewerUid = viewerUid)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👤",
                    style = MaterialTheme.typography.displaySmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text(
                text = user.fullName.ifBlank { "Unknown" },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = displayEmail,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            ProfileSection {
                ProfileRow(label = "Email", value = displayEmail)
                ProfileRow(label = "Phone", value = displayPhone)
                ProfileRow(label = "LinkedIn", value = displayLinkedIn)
                ProfileRow(label = "GitHub", value = displayGithub)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            Button(
                onClick = onSignOut,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = "Sign out",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
fun ProfileSection(
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            content()
        }
    }
}

@Composable
fun ProfileRow(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


