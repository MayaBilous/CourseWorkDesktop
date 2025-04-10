package org.example.project.domain.usecase

import org.example.project.domain.boundary.SportSectionListRepository
import org.example.project.domain.entity.SportSection


interface GetSectionDetails {

    suspend operator fun invoke(sectionDetailsId: Long): SportSection
}

class GetSectionDetailsUseCase(
    private val sportSectionListRepository: SportSectionListRepository
) : GetSectionDetails {

    override suspend fun invoke(sectionDetailsId: Long): SportSection {
        return sportSectionListRepository.getSportSectionDetails(sectionDetailsId)!!
    }
}
