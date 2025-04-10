package org.example.project.domain.boundary

import org.example.project.domain.entity.Login

interface AuthorizationRepository {

    suspend fun getLogins(): List<Login>
}