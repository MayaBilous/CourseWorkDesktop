package org.example.project.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbSportSection(
    @PrimaryKey
    val id: Long?,
    @ColumnInfo(name = "sectionName")
    val sectionName: String,
)
