package org.example.project.domain.usecase

import org.example.project.domain.boundary.SportSectionListRepository
import org.example.project.domain.entity.SportSection

interface GetSectionList {

    suspend operator fun invoke(): List<SportSection>
}

class GetSectionListUseCase(
    private val sportSectionListRepository: SportSectionListRepository
) : GetSectionList {

    override suspend fun invoke(): List<SportSection> {
        return sportSectionListRepository.getSportSections()
    }
}