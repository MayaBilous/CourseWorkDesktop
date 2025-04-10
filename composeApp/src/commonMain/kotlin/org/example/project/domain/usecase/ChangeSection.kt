package org.example.project.domain.usecase

import org.example.project.domain.boundary.SportSectionListRepository
import org.example.project.domain.entity.SportSection

interface ChangeSection {
    suspend operator fun invoke(sportSection: SportSection)
}

class ChangeSectionUseCase(
    private val sportSectionListRepository: SportSectionListRepository
) : ChangeSection {

    override suspend fun invoke(sportSection: SportSection) {
        sportSectionListRepository.updateSection(sportSection)
    }
}