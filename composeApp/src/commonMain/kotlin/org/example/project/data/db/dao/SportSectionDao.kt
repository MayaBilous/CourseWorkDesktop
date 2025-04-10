package org.example.project.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import org.example.project.data.db.model.DbSectionWithDetails
import org.example.project.data.db.model.DbSportSection

@Dao
interface SportSectionDao {

    @Update
    suspend fun updateSportSection(dbSportSection: DbSportSection)

    @Insert
    suspend fun addSportSection(vararg dbSportSection: DbSportSection)

    @Query("DELETE FROM DbSportSection WHERE id = :sportSectionId")
    suspend fun deleteSectionById(sportSectionId: Long)

    @Query("SELECT id FROM DbSportSection WHERE sectionName = :sectionName")
    suspend fun findSectionByName(sectionName: String): Long?

    @Transaction
    @Query("SELECT * FROM DbSportSection WHERE id = :id")
    suspend fun getSectionWithDetailsById(id: Long?): DbSectionWithDetails?


    @Transaction
    @Query("SELECT * FROM DbSportSection")
    suspend fun getSectionsWithDetails(): List<DbSectionWithDetails>


}