package org.example.project.domain.usecase

import org.example.project.domain.boundary.SportSectionListRepository
import org.example.project.domain.entity.SectionDetails

interface ChangeDetails {
    suspend operator fun invoke(details: SectionDetails)
}

class ChangeDetailsUseCase(
    private val sportSectionListRepository: SportSectionListRepository
) : ChangeDetails {

    override suspend fun invoke(details: SectionDetails) {
        sportSectionListRepository.updateDetails(details)
    }
}