package com.keshen.alumni_directory_app.data.repo

import com.keshen.alumni_directory_app.data.model.User

interface AlumniRepo {
    suspend fun getAllAlumni(): List<User>
    suspend fun getAlumniById(id: String): User?
}