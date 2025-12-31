package com.keshen.alumni_directory_app.data.repo

import com.keshen.alumni_directory_app.data.model.User

class AlumniFireRepo: AlumniRepo {
    override suspend fun getAllAlumni(): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getAlumniById(id: String): User? {
        TODO("Not yet implemented")
    }
}