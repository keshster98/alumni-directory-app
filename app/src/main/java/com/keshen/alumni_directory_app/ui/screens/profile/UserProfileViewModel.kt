package com.keshen.alumni_directory_app.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keshen.alumni_directory_app.core.utils.MessageType
import com.keshen.alumni_directory_app.core.utils.UiMessage
import com.keshen.alumni_directory_app.data.model.User
import com.keshen.alumni_directory_app.service.AuthService
import com.keshen.alumni_directory_app.service.UserProfileService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val authService: AuthService,
    private val profileService: UserProfileService
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private var originalUser: User? = null

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    private val _message = MutableStateFlow<UiMessage?>(null)
    val message = _message.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            runCatching {
                profileService.getProfile(authService.uid())
            }.onSuccess {
                originalUser = it
                _user.value = it
            }.onFailure {
                _message.value = UiMessage(it.message ?: "Failed to load profile", MessageType.ERROR)
            }
            _isLoading.value = false
        }
    }

    /* ───── Dirty checks ───── */

    fun isWorkDirty(e: User) =
        originalUser?.let {
            e.jobTitle != it.jobTitle ||
                    e.company != it.company ||
                    e.primaryTechStack != it.primaryTechStack
        } ?: false

    fun isLocationDirty(e: User) =
        originalUser?.let {
            e.city != it.city || e.country != it.country
        } ?: false

    fun isContactDirty(e: User) =
        originalUser?.let {
            e.phone != it.phone ||
                    e.github != it.github ||
                    e.linkedIn != it.linkedIn
        } ?: false

    fun isVisibilityDirty(e: User) =
        originalUser?.let {
            e.showPhone != it.showPhone ||
                    e.showLinkedIn != it.showLinkedIn ||
                    e.showGithub != it.showGithub
        } ?: false

    /* ───── Save helpers ───── */

    fun saveWork(e: User) = savePartial {
        profileService.updateWorkInfo(e.uid, e.jobTitle, e.company, e.primaryTechStack)
        originalUser = originalUser!!.copy(
            jobTitle = e.jobTitle,
            company = e.company,
            primaryTechStack = e.primaryTechStack
        )
    }

    fun saveLocation(e: User) = savePartial {
        profileService.updateLocation(e.uid, e.city, e.country)
        originalUser = originalUser!!.copy(city = e.city, country = e.country)
    }

    fun saveContact(e: User) = savePartial {
        profileService.updateContact(e.uid, e.phone, e.github, e.linkedIn)
        originalUser = originalUser!!.copy(
            phone = e.phone,
            github = e.github,
            linkedIn = e.linkedIn
        )
    }

    fun saveVisibility(e: User) = savePartial {
        profileService.updateVisibility(
            e.uid, e.showPhone, e.showLinkedIn, e.showGithub
        )
        originalUser = originalUser!!.copy(
            showPhone = e.showPhone,
            showLinkedIn = e.showLinkedIn,
            showGithub = e.showGithub
        )
    }

    private fun savePartial(block: suspend () -> Unit) {
        _isSaving.value = true
        viewModelScope.launch {
            runCatching { block() }
                .onSuccess {
                    _user.value = originalUser
                    _message.value = UiMessage("Changes saved", MessageType.SUCCESS)
                }
                .onFailure {
                    _message.value = UiMessage(it.message ?: "Save failed", MessageType.ERROR)
                }
            delay(1500)
            _message.value = null
            _isSaving.value = false
        }
    }
}