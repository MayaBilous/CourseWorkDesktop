package org.example.project.data.repository

import org.example.project.data.db.SectionDataBaseProvider
import org.example.project.data.repository.mapper.SportSectionMapper
import org.example.project.domain.boundary.SportSectionListRepository
import org.example.project.domain.entity.SectionDetails
import org.example.project.domain.entity.SportSection

class SportSectionListRepositoryImpl : SportSectionListRepository {

    private val mapper = SportSectionMapper()
    private val sportSectionDao = SectionDataBaseProvider.db.SportSectionDao()
    private val sectionDetailsDao = SectionDataBaseProvider.db.SectionDetailsDao()


    override suspend fun getSportSections(): List<SportSection> {
        return sportSectionDao.getSectionsWithDetails().map { mapper.mapToDomain(it) }
    }

    override suspend fun getSportSectionDetails(sectionId: Long): SportSection? {
        return sportSectionDao.getSectionWithDetailsById(sectionId)?.let { mapper.mapToDomain(it) }
    }

    override suspend fun deleteDetails(detailsId: Long) {
        sectionDetailsDao.deleteDetailsById(detailsId)
    }

    override suspend fun deleteSection(sectionId: Long) {
        sectionDetailsDao.deleteDetailsBySectionId(sectionId)
        sportSectionDao.deleteSectionById(sectionId)
    }

    override suspend fun updateSection(sportSection: SportSection) {
        sportSectionDao.updateSportSection(mapper.mapSectionToDb(sportSection))
    }

    override suspend fun updateDetails(details: SectionDetails) {
        sectionDetailsDao.updateDetails(mapper.mapDetailsToDb(details))
    }

    override suspend fun addSection(sportSection: SportSection) {
        sportSectionDao.addSportSection(mapper.mapSectionToDb(sportSection))
    }

    override suspend fun addDetails(sectionId: Long, details: SectionDetails) {
        val sectionDetails: SectionDetails = details.copy(sectionId = sectionId)
        sectionDetailsDao.addSectionDetails(mapper.mapDetailsToDb(sectionDetails))
    }

    override suspend fun getDetails(): List<SectionDetails> {
        return mapper.mapToDetailsList(sectionDetailsDao.getDetails())
    }
}

