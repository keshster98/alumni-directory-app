package com.keshen.alumni_directory_app.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UserProfileScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
//                AsyncImage(
//                    model = ImageRequest
//                        .Builder(context)
//                        .data(user.photoUrl)
//                        .build(),
//                    "",
//                    modifier = Modifier.fillMaxWidth(0.5f)
//                        .aspectRatio(1f)
//                        .clip(CircleShape)
//                )
            }

            item {
                ProfileSection(
                    color = android.R.color.holo_blue_light
                ) {
                    Text("Name")
                    Text("Email")
                    Text("Graduation Year")
                    Text("Department")
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp).fillMaxWidth())
                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )

                ) {
                    Text("Sign-out")
                }

            }
        }
    }
}
