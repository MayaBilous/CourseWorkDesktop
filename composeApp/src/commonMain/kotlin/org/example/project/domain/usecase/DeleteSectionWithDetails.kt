package org.example.project.domain.usecase


import org.example.project.domain.boundary.SportSectionListRepository
import org.example.project.domain.entity.SportSection

interface DeleteSectionWithDetails {
    suspend operator fun invoke(sportSection: SportSection)
}

class DeleteSectionWithDetailsUseCase(
    private val sportSectionListRepository: SportSectionListRepository
) : DeleteSectionWithDetails {

    override suspend fun invoke(sportSection: SportSection) {
        sportSectionListRepository.deleteSection(sportSection.id ?: 0)
    }
}