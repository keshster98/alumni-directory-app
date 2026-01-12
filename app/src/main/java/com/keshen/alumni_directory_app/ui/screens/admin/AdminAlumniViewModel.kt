package com.keshen.alumni_directory_app.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keshen.alumni_directory_app.core.utils.MessageType
import com.keshen.alumni_directory_app.core.utils.UiMessage
import com.keshen.alumni_directory_app.data.model.Status
import com.keshen.alumni_directory_app.data.model.User
import com.keshen.alumni_directory_app.service.UserProfileService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminAlumniEditViewModel @Inject constructor(
    private val profileService: UserProfileService
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    private val _message = MutableStateFlow<UiMessage?>(null)
    val message = _message.asStateFlow()

    fun load(uid: String) {
        viewModelScope.launch {
            runCatching {
                profileService.getProfile(uid)
            }.onSuccess {
                _user.value = it
            }.onFailure {
                _message.value = UiMessage(
                    it.message ?: "Failed to load alumni profile",
                    MessageType.ERROR
                )
            }
            _isLoading.value = false
        }
    }

    fun updateUser(updated: User) {
        _user.value = updated
    }

    fun saveProfile() {
        val current = _user.value ?: return
        _isSaving.value = true

        viewModelScope.launch {
            runCatching {
                profileService.saveProfile(current)
            }.onSuccess {
                _message.value = UiMessage(
                    "Profile updated successfully",
                    MessageType.SUCCESS
                )
            }.onFailure {
                _message.value = UiMessage(
                    it.message ?: "Failed to save profile",
                    MessageType.ERROR
                )
            }

            delay(1500)
            _message.value = null
            _isSaving.value = false
        }
    }

    fun updateStatus(status: Status) {
        val current = _user.value ?: return
        updateUser(current.copy(status = status))
        saveProfile()
    }
}
