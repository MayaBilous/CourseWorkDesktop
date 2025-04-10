package org.example.project.domain.usecase

import org.example.project.domain.boundary.SportSectionListRepository
import org.example.project.domain.entity.SectionDetails

interface AddDetails {
    suspend operator fun invoke(sectionId: Long, details: SectionDetails)
}

class AddDetailsUseCase(
    private val sportSectionListRepository: SportSectionListRepository
) : AddDetails {

    override suspend fun invoke(sectionId: Long, details: SectionDetails) {
        sportSectionListRepository.addDetails(sectionId, details)
    }
}