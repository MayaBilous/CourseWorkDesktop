package org.example.project.domain.usecase

import org.example.project.domain.boundary.SportSectionListRepository
import org.example.project.domain.entity.SportSection

interface AddSection {
    suspend operator fun invoke(sportSection: SportSection)
}

class AddSectionUseCase(
    private val sportSectionListRepository: SportSectionListRepository
) : AddSection {

    override suspend fun invoke(sportSection: SportSection) {
        sportSectionListRepository.addSection(sportSection)
    }
}