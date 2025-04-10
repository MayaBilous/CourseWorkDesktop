package org.example.project.data.db.model

import androidx.room.Embedded
import androidx.room.Relation

data class DbSectionWithDetails(
    @Embedded
    val sportSection: DbSportSection,
    @Relation(
        parentColumn = "id",
        entityColumn = "sectionId"
    )
    val sectionDetails: List<DbSectionDetails>
)
