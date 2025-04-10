package org.example.project.data.repository.mapper

import org.example.project.data.db.model.DbLogin
import org.example.project.domain.entity.Login

class LoginMapper {
    fun mapToDomain(dbLogin: DbLogin): Login {
        return Login(
            userName = dbLogin.userName,
            password = dbLogin.password,
            isAdmin = dbLogin.isAdmin,
        )
    }
}