package com.keshen.alumni_directory_app.data.repo

import com.keshen.alumni_directory_app.data.model.RegistrationForm

interface AlumniRepo {
    suspend fun getAllAlumni(): List<RegistrationForm>
    suspend fun getAlumniById(id: String): RegistrationForm?
}