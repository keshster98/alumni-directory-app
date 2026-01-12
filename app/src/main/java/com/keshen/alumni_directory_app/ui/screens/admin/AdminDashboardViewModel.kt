package com.keshen.alumni_directory_app.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keshen.alumni_directory_app.data.model.User
import com.keshen.alumni_directory_app.service.UserProfileService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val profileService: UserProfileService
) : ViewModel() {
    private var allUsers: List<User> = emptyList()

    val users = MutableStateFlow<List<User>>(emptyList())
    val isLoading = MutableStateFlow(true)
    val error = MutableStateFlow<String?>(null)

    val searchQuery = MutableStateFlow("")

    val techOptions = MutableStateFlow<List<String>>(emptyList())
    val cityOptions = MutableStateFlow<List<String>>(emptyList())
    val countryOptions = MutableStateFlow<List<String>>(emptyList())
    val yearOptions = MutableStateFlow<List<Int>>(emptyList())
    val statusOptions = listOf("APPROVED", "PENDING", "REJECTED", "INACTIVE")

    val selectedTech = MutableStateFlow<String?>(null)
    val selectedCity = MutableStateFlow<String?>(null)
    val selectedCountry = MutableStateFlow<String?>(null)
    val selectedYear = MutableStateFlow<Int?>(null)
    val selectedStatus = MutableStateFlow<String?>(null)

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            runCatching {
                profileService.getAllUsers()
            }.onSuccess { result ->
                allUsers = result
                buildOptions(result)
                applyFilters()
                isLoading.value = false
            }.onFailure {
                error.value = it.message
                isLoading.value = false
            }
        }
    }

    private fun buildOptions(users: List<User>) {
        techOptions.value = users.map { it.primaryTechStack }.distinct().sorted()
        cityOptions.value = users.map { it.city }.distinct().sorted()
        countryOptions.value = users.map { it.country }.distinct().sorted()
        yearOptions.value = users.map { it.graduationYear }.distinct().sorted()
    }

    fun onSearch(query: String) {
        searchQuery.value = query
        applyFilters()
    }

    fun applyFilters() {
        users.value = allUsers.filter { user ->
            (searchQuery.value.isBlank() ||
                    user.fullName.contains(searchQuery.value, true))

                    && (selectedTech.value == null || user.primaryTechStack == selectedTech.value)
                    && (selectedCity.value == null || user.city == selectedCity.value)
                    && (selectedCountry.value == null || user.country == selectedCountry.value)
                    && (selectedYear.value == null || user.graduationYear == selectedYear.value)
                    && (selectedStatus.value == null || user.status.name == selectedStatus.value)
        }
    }

    fun clearFilters() {
        selectedTech.value = null
        selectedCity.value = null
        selectedCountry.value = null
        selectedYear.value = null
        selectedStatus.value = null
        applyFilters()
    }
}
