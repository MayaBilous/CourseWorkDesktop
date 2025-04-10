package org.example.project.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbSectionDetails (
    @PrimaryKey
    val id: Long?,
    @ColumnInfo(name = "sectionId")
    val sectionId: Long?,
    @ColumnInfo(name = "address")
    val address: String,
    @ColumnInfo(name = "workingDays")
    val workingDays: String,
    @ColumnInfo(name = "phoneNumber")
    val phoneNumber: String,
    @ColumnInfo(name = "price")
    val price: Int,
    @ColumnInfo(name = "isSelected")
    val isSelected: Boolean,
)