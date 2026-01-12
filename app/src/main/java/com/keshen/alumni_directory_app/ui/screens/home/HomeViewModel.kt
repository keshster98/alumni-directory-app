package com.keshen.alumni_directory_app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keshen.alumni_directory_app.data.model.User
import com.keshen.alumni_directory_app.service.AuthService
import com.keshen.alumni_directory_app.service.UserProfileService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val profileService: UserProfileService,
    private val authService: AuthService
) : ViewModel() {
    private var allUsers: List<User> = emptyList()
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()


    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    val searchQuery = MutableStateFlow("")
    val techOptions = MutableStateFlow<List<String>>(emptyList())
    val countryOptions = MutableStateFlow<List<String>>(emptyList())
    val yearOptions = MutableStateFlow<List<Int>>(emptyList())
    val selectedTech = MutableStateFlow<String?>(null)
    val selectedCountry = MutableStateFlow<String?>(null)
    val selectedYear = MutableStateFlow<Int?>(null)

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            runCatching {
                profileService.getApprovedUsers()
            }.onSuccess { result ->
                val currentUid = authService.uid()
                allUsers = result.filter { user ->
                    user.uid != currentUid
                }
                buildFilterOptions(result)
                applyFilters()
                _isLoading.value = false
            }.onFailure {
                _error.value = it.message
                _isLoading.value = false
            }
        }
    }

    private fun buildFilterOptions(users: List<User>) {
        techOptions.value =
            users.map { it.primaryTechStack }
                .filter { it.isNotBlank() }
                .distinct()
                .sorted()

        countryOptions.value =
            users.map { it.country }
                .filter { it.isNotBlank() }
                .distinct()
                .sorted()

        yearOptions.value =
            users.map { it.graduationYear }
                .distinct()
                .sortedDescending()
    }

    fun onSearch(query: String) {
        searchQuery.value = query
        applyFilters()
    }

    fun onFilterChanged() {
        applyFilters()
    }

    fun clearFilters() {
        selectedTech.value = null
        selectedCountry.value = null
        selectedYear.value = null
        applyFilters()
    }

    private fun applyFilters() {
        val q = searchQuery.value.trim()

        _users.value = allUsers.filter { user ->

            val matchesSearch =
                q.isEmpty() ||
                        user.fullName.contains(q, true) ||
                        user.graduationYear.toString().contains(q)

            val matchesTech =
                selectedTech.value == null ||
                        user.primaryTechStack == selectedTech.value

            val matchesCountry =
                selectedCountry.value == null ||
                        user.country == selectedCountry.value

            val matchesYear =
                selectedYear.value == null ||
                        user.graduationYear == selectedYear.value

            matchesSearch &&
                    matchesTech &&
                    matchesCountry &&
                    matchesYear
        }
    }
}