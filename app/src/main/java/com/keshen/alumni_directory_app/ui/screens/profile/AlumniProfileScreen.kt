package com.keshen.alumni_directory_app.ui.screens.profile
//
//import android.content.ActivityNotFoundException
//import android.content.Intent
//import android.net.Uri
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.ColumnScope
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import com.keshen.alumni_directory_app.data.model.User
//
//@Composable
//fun AlumniProfileScreen(
//    user: User,
//    viewerUid: String,
//    viewerIsAdmin: Boolean
//) {
//    val context = LocalContext.current
//
//    val displayEmail = user.displayEmail(viewerIsAdmin = viewerIsAdmin, viewerUid = viewerUid)
//    val displayPhone = user.displayPhone(viewerIsAdmin = viewerIsAdmin, viewerUid = viewerUid)
//    val displayLinkedIn = user.displayLinkedIn(viewerIsAdmin = viewerIsAdmin, viewerUid = viewerUid)
//    val displayGithub = user.displayGithub(viewerIsAdmin = viewerIsAdmin, viewerUid = viewerUid)
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(24.dp)
//    ) {
//        LazyColumn(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            item {
//                // Optional: profile picture block (kept commented in your original)
//            }
//
//            item {
//                ProfileSection(
//                    color = android.R.color.holo_blue_light
//                ) {
//                    Text("Name: ${user.fullName.ifBlank { "Unknown" }}")
//                    Text("Graduation Year: ${user.graduationYear}")
//                    Text("Department: ${user.department.ifBlank { "-" }}")
//                }
//            }
//
//            item {
//                ProfileSection(
//                    color = android.R.color.holo_green_light
//                ) {
//                    Text("Current Job Title: ${user.jobTitle.ifBlank { "-" }}")
//                    Text("Company: ${user.company.ifBlank { "-" }}")
//                    Text("Tech Stack: ${user.techStack.ifBlank { "-" }}")
//                }
//            }
//
//            item {
//                ProfileSection(
//                    color = android.R.color.system_secondary_light
//                ) {
//                    Text("City: ${user.city.ifBlank { "-" }}")
//                    Text("Country: ${user.country.ifBlank { "-" }}")
//                }
//            }
//
//            item {
//                ProfileSection(color = android.R.color.holo_orange_light) {
//                    ContactRow(
//                        label = "Email",
//                        value = displayEmail,
//                        enabled = isActionableDisplayValue(displayEmail),
//                        onClick = {
//                            launchSafe(context, mailtoIntent(displayEmail))
//                        }
//                    )
//                    ContactRow(
//                        label = "Phone",
//                        value = displayPhone,
//                        enabled = isActionableDisplayValue(displayPhone),
//                        onClick = {
//                            launchSafe(context, dialIntent(displayPhone))
//                        }
//                    )
//                    ContactRow(
//                        label = "LinkedIn",
//                        value = displayLinkedIn,
//                        enabled = isActionableDisplayValue(displayLinkedIn),
//                        onClick = {
//                            launchSafe(context, viewUrlIntent(displayLinkedIn))
//                        }
//                    )
//                    ContactRow(
//                        label = "GitHub",
//                        value = displayGithub,
//                        enabled = isActionableDisplayValue(displayGithub),
//                        onClick = {
//                            launchSafe(context, viewUrlIntent(displayGithub))
//                        }
//                    )
//                }
//
////            item {
////                ProfileSection(
////                    color = android.R.color.holo_red_light
////                ) {
////                    Text("Past Job History: -")
////                    Text("Skills / Tags: -")
////                    Text("Bio: ${user.bio.ifBlank { "-" }}")
////                }
////            }
//            }
//        }
//    }
//}
//
//@Composable
//private fun ContactRow(
//    label: String,
//    value: String,
//    enabled: Boolean,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Column(
//        modifier = modifier
//            .fillMaxWidth()
//            .clickable(enabled = enabled) { onClick() }
//            .padding(vertical = 6.dp)
//    ) {
//        Text(
//            text = label,
//            style = MaterialTheme.typography.labelMedium
//        )
//        Text(
//            text = value.ifBlank { "-" },
//            style = MaterialTheme.typography.bodyLarge,
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis,
//            color = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
//        )
//    }
//}
//
//private fun isActionableDisplayValue(value: String): Boolean {
//    val v = value.trim()
//    if (v.isEmpty()) return false
//    val lowered = v.lowercase()
//    if (lowered == "hide") return false
//    if (lowered == "-") return false
//    return true
//}
//
//private fun mailtoIntent(email: String): Intent {
//    return Intent(Intent.ACTION_SENDTO).apply {
//        data = Uri.parse("mailto:${email.trim()}")
//        putExtra(Intent.EXTRA_EMAIL, arrayOf(email.trim()))
//    }
//}
//
//private fun dialIntent(phone: String): Intent {
//    val normalized = phone.trim()
//    return Intent(Intent.ACTION_DIAL).apply {
//        data = Uri.parse("tel:$normalized")
//    }
//}
//
//private fun viewUrlIntent(rawUrl: String): Intent {
//    val trimmed = rawUrl.trim()
//    val url = if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) trimmed else "https://$trimmed"
//    return Intent(Intent.ACTION_VIEW, Uri.parse(url))
//}
//
//private fun launchSafe(context: android.content.Context, intent: Intent) {
//    try {
//        context.startActivity(intent)
//    } catch (_: ActivityNotFoundException) {
//        // No handler installed; ignore.
//    }
//}
//
//
//@Composable
//fun ProfileSection(
//    color: Int,
//    content: @Composable ColumnScope.() -> Unit
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp)
//            .background(
//                color = colorResource(id = color),
//                shape = RoundedCornerShape(16.dp)
//            )
//            .padding(16.dp)
//    ) {
//        content()
//    }
//}
//
