package com.keshen.alumni_directory_app.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keshen.alumni_directory_app.data.model.User
import com.keshen.alumni_directory_app.service.UserProfileService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlumniProfileViewModel @Inject constructor(
    private val profileService: UserProfileService
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun load(uid: String) {
        viewModelScope.launch {
            runCatching {
                profileService.getProfile(uid)
            }.onSuccess {
                _user.value = it
            }.onFailure {
                _error.value = it.message ?: "Failed to load profile"
            }
            _isLoading.value = false
        }
    }
}
