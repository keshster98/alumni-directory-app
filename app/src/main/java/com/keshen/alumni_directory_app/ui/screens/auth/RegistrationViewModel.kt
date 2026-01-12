package com.keshen.alumni_directory_app.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keshen.alumni_directory_app.core.utils.MessageType
import com.keshen.alumni_directory_app.core.utils.UiMessage
import com.keshen.alumni_directory_app.data.model.User
import com.keshen.alumni_directory_app.service.UserProfileService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val profileService: UserProfileService
) : ViewModel() {

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting = _isSubmitting.asStateFlow()

    private val _message = MutableStateFlow<UiMessage?>(null)
    val message = _message.asStateFlow()

    fun submitRegistration(
        user: User,
        onSuccess: () -> Unit
    ) {
        _message.value = null
        _isSubmitting.value = true

        if (!isValid(user)) {
            showError("Please fill in all the fields!")
            return
        }

        saveProfile(user, onSuccess)
    }

    private fun saveProfile(
        user: User,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            runCatching {
                profileService.saveProfile(user)
            }.onSuccess {
                _message.value = UiMessage(
                    "Form submitted successfully, ${user.fullName}! Redirecting you to the status page.",
                    MessageType.SUCCESS
                )
                delay(1500)
                onSuccess()
            }.onFailure {
                showError(it.message ?: "Failed to submit registration. Please try again shortly.")
            }
        }
    }

    private fun isValid(user: User): Boolean {
        return user.fullName.isNotBlank() &&
                user.email.isNotBlank() &&
                user.phone.isNotBlank() &&
                user.graduationYear > 0 &&
                user.department.isNotBlank() &&
                user.jobTitle.isNotBlank() &&
                user.company.isNotBlank() &&
                user.primaryTechStack.isNotBlank() &&
                user.city.isNotBlank() &&
                user.country.isNotBlank() &&
                user.linkedIn.isNotBlank() &&
                user.github.isNotBlank()
    }

    private fun showError(text: String) {
        _message.value = UiMessage(text, MessageType.ERROR)
        _isSubmitting.value = false
        clearMessage()
    }

    private fun clearMessage() {
        viewModelScope.launch {
            delay(2000)
            _message.value = null
        }
    }
}
