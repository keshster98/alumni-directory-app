package com.keshen.alumni_directory_app.data.repo

import com.keshen.alumni_directory_app.data.model.RegistrationForm

class AlumniFireRepo: AlumniRepo {
    override suspend fun getAllAlumni(): List<RegistrationForm> {
        TODO("Not yet implemented")
    }

    override suspend fun getAlumniById(id: String): RegistrationForm? {
        TODO("Not yet implemented")
    }
}