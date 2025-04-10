package org.example.project.data.repository

import org.example.project.data.db.SectionDataBaseProvider
import org.example.project.data.repository.mapper.LoginMapper
import org.example.project.domain.boundary.AuthorizationRepository
import org.example.project.domain.entity.Login

class AuthorizationRepositoryImpl : AuthorizationRepository {

    private val mapper = LoginMapper()
    private val loginDao = SectionDataBaseProvider.db.LoginDao()

    override suspend fun getLogins(): List<Login> {
        return loginDao.getAll().map { mapper.mapToDomain(it) }
    }
}