package org.example.project.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbLogin(
    @PrimaryKey val id: Long?,
    @ColumnInfo(name = "userName") val userName: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "isAdmin") val isAdmin: Boolean,
)
