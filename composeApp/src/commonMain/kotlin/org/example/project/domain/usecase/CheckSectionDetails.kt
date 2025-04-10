package org.example.project.domain.usecase

import org.example.project.domain.entity.SectionDetails

interface CheckSectionDetails {

    suspend operator fun invoke(sectionDetails: SectionDetails): Boolean
}

class CheckSectionDetailsUseCase(
) : CheckSectionDetails {

    override suspend fun invoke(sectionDetails: SectionDetails): Boolean {
        if (sectionDetails.price <= 0 ||
            sectionDetails.address.isEmpty() ||
            sectionDetails.workingDays.isEmpty() ||
            sectionDetails.phoneNumber.isEmpty()
        ) {
            return false
        } else {
            return true
        }
    }
}