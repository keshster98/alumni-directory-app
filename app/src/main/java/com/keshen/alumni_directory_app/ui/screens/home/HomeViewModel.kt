package com.keshen.alumni_directory_app.ui.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keshen.alumni_directory_app.data.model.User
import com.keshen.alumni_directory_app.service.UserProfileService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val profileService: UserProfileService
) : ViewModel() {

    private var allUsers: List<User> = emptyList()

    val users = mutableStateOf<List<User>>(emptyList())
    val searchQuery = mutableStateOf("")
    val isLoading = mutableStateOf(true)
    val error = mutableStateOf<String?>(null)

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            runCatching {
                profileService.getApprovedUsers()
            }.onSuccess { result ->
                allUsers = result
                applySearch()
                isLoading.value = false
            }.onFailure {
                error.value = it.message
                isLoading.value = false
            }
        }
    }

    fun onSearch(query: String) {
        searchQuery.value = query
        applySearch()
    }

    private fun applySearch() {
        val q = searchQuery.value.trim()

        users.value =
            if (q.isEmpty()) {
                allUsers
            } else {
                allUsers.filter {
                    it.fullName.contains(q, ignoreCase = true) ||
                            it.graduationYear.toString().contains(q, ignoreCase = true)
                }
            }
    }
}