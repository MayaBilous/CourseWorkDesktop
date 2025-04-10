package org.example.project.domain.boundary

import org.example.project.domain.entity.SectionDetails
import org.example.project.domain.entity.SportSection


interface SportSectionListRepository {

    suspend fun getSportSections(): List<SportSection>

    suspend fun getSportSectionDetails(sectionId: Long): SportSection?

    suspend fun deleteDetails(detailsId: Long)

    suspend fun deleteSection(sectionId: Long)

    suspend fun updateSection(sportSection: SportSection)

    suspend fun updateDetails(details: SectionDetails)

    suspend fun addSection(sportSection: SportSection)

    suspend fun addDetails(sectionId: Long, details: SectionDetails)

    suspend fun getDetails(): List<SectionDetails>


}
