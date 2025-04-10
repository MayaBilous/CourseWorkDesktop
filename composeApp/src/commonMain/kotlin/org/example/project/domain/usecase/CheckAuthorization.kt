package org.example.project.domain.usecase

import org.example.project.domain.boundary.AuthorizationRepository
import org.example.project.domain.entity.AuthResult
import org.example.project.domain.entity.Login

interface CheckAuthorization {

    suspend operator fun invoke(login: Login): AuthResult
}

class CheckAuthorizationUseCase(
    private val authorizationRepository: AuthorizationRepository,
) : CheckAuthorization {

    override suspend fun invoke(login: Login): AuthResult {
        if (login.userName.isEmpty() || login.password.isEmpty()) {
            return AuthResult.EMPTY
        }

        return authorizationRepository.getLogins().find {
            it.userName == login.userName &&
                    it.password == login.password
        }?.let {
            if (it.isAdmin) AuthResult.ADMIN else AuthResult.USER
        } ?: AuthResult.INCORRECT
    }
}
