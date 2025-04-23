package org.example.project.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import org.example.project.data.db.dao.SportSectionDao
import org.example.project.data.db.dao.LoginDao
import org.example.project.data.db.dao.SectionDetailsDao
import org.example.project.data.db.model.DbSportSection
import org.example.project.data.db.model.DbLogin
import org.example.project.data.db.model.DbSectionDetails

@Database([DbSportSection::class, DbSectionDetails::class, DbLogin::class], version = 4)
abstract class SectionDataBase : RoomDatabase() {
    abstract fun LoginDao(): LoginDao
    abstract fun SportSectionDao(): SportSectionDao
    abstract fun SectionDetailsDao(): SectionDetailsDao

    companion object {

        const val DB_NAME = "app-persistence.db"
        const val DB_VERSION = 1
    }
}