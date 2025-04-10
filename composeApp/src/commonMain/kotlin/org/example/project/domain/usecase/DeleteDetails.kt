package org.example.project.domain.usecase

import org.example.project.domain.boundary.SportSectionListRepository

interface DeleteDetails {
    suspend operator fun invoke(detailsId: Long)
}

class DeleteDetailsUseCase(
    private val sportSectionListRepository: SportSectionListRepository
) : DeleteDetails {

    override suspend fun invoke(detailsId: Long) {
        sportSectionListRepository.deleteDetails(detailsId)
    }
}